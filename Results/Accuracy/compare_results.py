import argparse
import csv
import matplotlib.pyplot as plt
import sys


class StateTransition(object):
    def __init__(self, from_state, to_state, time):
        self.from_state = from_state
        self.to_state = to_state
        self.time = time

    def __repr__(self):
        return "Transition [from: {}, to: {}, time: {}]".format(self.from_state, self.to_state, self.time)


class AircraftState(object):
    def __init__(self, state_time, error):
        self.state_time = state_time
        self.error = error


class Prediction(object):
    def __init__(self, aircraft_id, prediction_time, prediction_state):
        self.aircraft_id = aircraft_id
        self.prediction_time = prediction_time
        self.states = []
        self.prediction_state = prediction_state

    def add_state(self, state):
        self.states.append(state)

    def total_error(self):
        total = 0.0
        for state in self.states:
            total += state.error

        return total

    def average_error(self):
        return self.total_error()/float(len(self.states))

    def weighted_error(self):
        """
        Error that has been weighted so the initial errors are more important
        than the final errors.
        """

        total = 0.0
        i = 1.0
        for state in self.states:
            i += 1.0
            total += state.error/i

        return total/float(len(self.states))


class ErrorCalculator(object):
    def run(self, prediction):
        pass

    def name(self):
        pass


class AverageErrorCalculator(ErrorCalculator):
    def run(self, prediction):
        return prediction.average_error()

    def name(self):
        return "Average Error"


class TotalErrorCalculator(ErrorCalculator):
    def run(self, prediction):
        return prediction.total_error()

    def name(self):
        return "Total Error"


class WeightedErrorCalculator(ErrorCalculator):
    def run(self, prediction):
        return prediction.weighted_error()

    def name(self):
        return "Early Weighted Error"


class ErrorLog:
    def __init__(self, name, error_calculator):
        self.name = name
        self.predictions = []
        self.error_calculator = error_calculator

    def add_prediction(self, prediction):
        self.predictions.append(prediction)

    def bounds(self):
        min_time = sys.maxsize
        max_time = 0
        min_error = sys.maxsize
        max_error = 0

        for prediction in self.predictions:
            if prediction.prediction_time > max_time:
                max_time = prediction.prediction_time

            if prediction.prediction_time < min_time:
                min_time = prediction.prediction_time

            error = self.error_calculator.run(prediction)
            if error > max_error:
                max_error = error

            if error < min_error:
                min_error = error

        return (min_time, max_time, min_error, max_error)

    def get_transitions(self):
        transitions = []
        prev_state = None
        for prediction in self.predictions:
            curr_state = prediction.prediction_state
            if prev_state != curr_state:
                transition = StateTransition(prev_state,
                                             curr_state,
                                             prediction.prediction_time)
                transitions.append(transition)
                prev_state = curr_state

        return transitions

    def get_error(self, prediction_time):
        for prediction in self.predictions:
            if prediction.prediction_time == prediction_time:
                return self.error_calculator.run(prediction)


def run(args):
    selected_aircraft_id = args.aircraft_id

    error_calculator = None

    if args.error_calc_method == "Weighted":
        error_calculator = WeightedErrorCalculator()
    elif args.error_calc_method == "Average":
        error_calculator = AverageErrorCalculator()
    elif args.error_calc_method == "Total":
        error_calculator = TotalErrorCalculator()
    else:
        raise "Invalid error calculation method: " + args.error_calc_method

    logs = []

    # read them
    for filename in args.files:
        log = ErrorLog(filename, error_calculator)
        print("reading: " + filename)
        with open(filename, 'r') as csvfile:
            reader = csv.reader(csvfile, delimiter=',')
            for row in reader:
                aircraft_id = row[0]
                if selected_aircraft_id is None:
                    selected_aircraft_id = aircraft_id

                if aircraft_id == selected_aircraft_id:
                    prediction_state = row[1].strip()
                    prediction_time = int(row[2])

                    prediction = Prediction(aircraft_id,
                                            prediction_time,
                                            prediction_state)

                    i = 2
                    while i+2 < len(row):
                        state_time = int(row[i])
                        error = float(row[i+1])
                        state = AircraftState(state_time=state_time,
                                              error=error)
                        prediction.add_state(state)
                        i += 2

                    log.add_prediction(prediction)

        logs.append(log)

    # graph them
    line_handles = []

    for log in logs:
        bounds = log.bounds()

        x_axis = []
        y_axis = []

        for prediction in log.predictions:
            x_axis.append(prediction.prediction_time)
            y_axis.append(log.error_calculator.run(prediction))

        line_handle, = plt.plot(x_axis, y_axis, label=log.name)
        line_color = line_handle.get_color()

        line_handles.append(line_handle)

        transitions = log.get_transitions()
        marker_plots = {}
        for transition in transitions:
            y = log.get_error(transition.time)
            x = transition.time
            to_state = transition.to_state

            marker_style = "."

            if to_state == "RIGHT_TURN":
                marker_style = ">"
            elif to_state == "LEFT_TURN":
                marker_style = "<"
            elif to_state == "STRAIGHT":
                marker_style = "^"
            else:
                marker_style = "."

            marker_style += line_color

            if marker_style not in marker_plots:
                marker_plots[marker_style] = [[], []]

            marker_plots[marker_style][0].append(x)
            marker_plots[marker_style][1].append(y)

        for key, val in marker_plots.items():
            label = None
            if "^" in key:
                label = "state: STRAIGHT"
            elif ">" in key:
                label = "state: RIGHT_TURN"
            elif "<" in key:
                label = "state: LEFT_TURN"

            line_handle, = plt.plot(val[0], val[1], key, label=label)

            line_handles.append(line_handle)

    plt.axis([bounds[0], bounds[1], 0, args.upper_error_bound])
    plt.legend(handles=line_handles)
    plt.title("Plot of aircraft " + selected_aircraft_id)
    plt.ylabel(error_calculator.name())
    plt.xlabel("Time")
    plt.show()


if __name__ == "__main__":
    error_calc_choices = ["Weighted",
                          "Average",
                          "Total"]
    parser = argparse.ArgumentParser(
        formatter_class=argparse.ArgumentDefaultsHelpFormatter,
        description="""Compare integration test results.""",
        epilog="""Notes: Default aircraft for analysis is the first that appears.
        """)
    parser.add_argument('-a', '--aircraft-id', type=str, dest='aircraft_id',
                        metavar='ID',
                        nargs='?',
                        help='id of aircraft you want to select for analysis')
    parser.add_argument('-u', '--upper-error-bound',
                        type=int,
                        dest='upper_error_bound',
                        metavar='value',
                        nargs='?',
                        default=10000,
                        help='The upper error bound (for display)')
    parser.add_argument('-e', '--error-calc',
                        metavar='Method',
                        dest='error_calc_method',
                        nargs='?',
                        choices=error_calc_choices,
                        default=error_calc_choices[1],
                        help="Method for error calculation. Allowed values: " +
                        ", ".join(error_calc_choices))
    parser.add_argument('files',
                        metavar='File',
                        type=str,
                        nargs='+',
                        help="Files for analysis/comparison")

    args = parser.parse_args()
    run(args)
