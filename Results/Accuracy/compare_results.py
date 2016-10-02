import argparse
import csv
import json
import matplotlib.pyplot as plt
import sys
from matplotlib.widgets import Slider, RadioButtons
import matplotlib.gridspec as gridspec


class StateTransition(object):
    def __init__(self, from_state, to_state, time):
        self.from_state = from_state
        self.to_state = to_state
        self.time = time

    def __repr__(self):
        return "Transition [from: {}, to: {}, time: {}]".format(self.from_state, self.to_state, self.time)


class AircraftState(object):
    def __init__(self, state_time, error, position):
        self.state_time = state_time
        self.error = error
        self.position = position


class Prediction(object):
    def __init__(self,
                 aircraft_id,
                 prediction_time,
                 prediction_state,
                 current_position):
        self.aircraft_id = aircraft_id
        self.prediction_time = prediction_time
        self.states = []
        self.prediction_state = prediction_state
        self.current_position = current_position

    def add_state(self, state):
        self.states.append(state)

    def total_error(self):
        total = 0.0
        for state in self.states:
            total += state.error

        return total

    def average_error(self):
        if len(self.states) == 0:
            return 0

        return self.total_error()/float(len(self.states))

    def weighted_error(self):
        """
        Error that has been weighted so the initial errors are more important
        than the final errors.
        """

        if len(self.states) == 0:
            return 0

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
        self.color = None

    def add_prediction(self, prediction):
        self.predictions.append(prediction)

    def error_bounds(self):
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

    def pos_bounds(self):
        min_x = sys.maxsize
        max_x = 0
        min_y = sys.maxsize
        max_y = 0

        for prediction in self.predictions:
            pos = prediction.current_position
            x = pos[0]
            y = pos[1]
            if x > max_x:
                max_x = x
            if x < min_x:
                min_x = x
            if y > max_y:
                max_y = y
            if y < min_y:
                min_y = y

        return (min_x, max_x, min_y, max_y)

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
        with open(filename, 'r') as json_file:
            json_data = json.load(json_file)
            for json_prediction in json_data:
                aircraft_id = json_prediction["plane-id"]
                if selected_aircraft_id is None:
                    selected_aircraft_id = aircraft_id

                if aircraft_id == selected_aircraft_id:
                    prediction_state = json_prediction["state"]
                    prediction_time = json_prediction["time"]

                    json_pos = json_prediction["current-position"]
                    current_position = [json_pos[0], json_pos[1]]

                    prediction = Prediction(aircraft_id,
                                            prediction_time,
                                            prediction_state,
                                            current_position
                                            )

                    # prediction positions start after the current position
                    for json_track_item in json_prediction["prediction-track"]:
                        state_time = json_track_item["time"]
                        error = json_track_item["error-distance"]
                        json_track_item_pos = json_track_item["position"]
                        state = AircraftState(state_time=state_time,
                                              error=error,
                                              position=json_track_item_pos)
                        prediction.add_state(state)

                    log.add_prediction(prediction)

        logs.append(log)

    # graph them
    gs = gridspec.GridSpec(3, 1, height_ratios=[8, 8, 1])
    figure_1 = plt.figure(1)
    figure_1.suptitle("Plot of Aircraft: " + selected_aircraft_id, fontsize=20)
    subplot_1 = plt.subplot(gs[0])
    line_handles = []

    for log in logs:
        x_axis = []
        y_axis = []

        for prediction in log.predictions:
            x_axis.append(prediction.prediction_time)
            print("number of states for " + log.name + ": " + str(len(prediction.states)) + " + error: " + str(log.error_calculator.run(prediction)))
            y_axis.append(log.error_calculator.run(prediction))

        line_handle, = plt.plot(x_axis, y_axis, label=log.name)
        line_color = line_handle.get_color()
        log.color = line_color

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

    log = logs[0]
    error_bounds = log.error_bounds()
    plt.axis([error_bounds[0], error_bounds[1], 0, args.upper_error_bound])
    plt.legend(handles=line_handles)
    plt.title("Error", fontsize=16)
    plt.ylabel(error_calculator.name())

    half_time = error_bounds[0] + (error_bounds[1]-error_bounds[0])/2
    time_line_handle = plt.axvline(error_bounds[0],
                                   color='grey',
                                   linestyle='--')

    # plot aircraft position
    subplot_2 = plt.subplot(gs[1])
    plt.title("Position", fontsize=16)

    pos_bounds = log.pos_bounds()

    pos_x_axis = []
    pos_y_axis = []
    pos_time_axis = []

    for prediction in log.predictions:
        pos = prediction.current_position
        pos_x_axis.append(pos[0])
        pos_y_axis.append(pos[1])
        pos_time_axis.append(prediction.prediction_time)

    def lerp(p1, p2, t):
        return (1-t)*p1 + t*p2

    def nearest_position(time):
        for i in range(0, len(pos_time_axis), 1):
            t = pos_time_axis[i]
            if time < t:
                if i == 0:
                    return (pos_x_axis[i], pos_y_axis[i])

                dt_total = t - pos_time_axis[i-1]
                dt = time - pos_time_axis[i-1]
                lerp_t = float(dt)/float(dt_total)

                x1 = pos_x_axis[i-1]
                y1 = pos_y_axis[i-1]
                x2 = pos_x_axis[i]
                y2 = pos_y_axis[i]

                x = lerp(x1, x2, lerp_t)
                y = lerp(y1, y2, lerp_t)

                return (x, y)

        return (pos_x_axis[-1], pos_y_axis[-1])

    def nearest_time_index(time):
        for i in range(0, len(pos_time_axis), 1):
            t = pos_time_axis[i]
            if time < t:
                return i

        return len(pos_time_axis) - 1

    curr_pos_x = [pos_x_axis[0]]
    curr_pos_y = [pos_y_axis[0]]

    log_pred_axis = {}

    pos_line_handle, = plt.plot(pos_x_axis, pos_y_axis, label="Aircraft Track", color='red')
    curr_pos_line_handle, = plt.plot(curr_pos_x, curr_pos_y, 'ro', label="Current Position")
    handles = [pos_line_handle, curr_pos_line_handle]

    prediction_line_handles = {}

    def gen_prediction_axis(prediction):
        pred_x_axis = [prediction.current_position[0]]
        pred_y_axis = [prediction.current_position[1]]

        for state in prediction.states:
            pred_x_axis.append(state.position[0])
            pred_y_axis.append(state.position[1])

        return pred_x_axis, pred_y_axis

    for log in logs:
        prediction = log.predictions[0]

        pred_x_axis, pred_y_axis = gen_prediction_axis(prediction)

        axis = {"x": pred_x_axis, "y": pred_y_axis}
        log_pred_axis[log.name] = axis

        prediction_line_handle, = plt.plot(pred_x_axis, pred_y_axis, label=log.name, color=log.color)
        handles.append(prediction_line_handle)
        prediction_line_handles[log.name] = prediction_line_handle



    plt.legend(handles=handles)

    plt.axis([pos_bounds[0], pos_bounds[1], pos_bounds[2], pos_bounds[3]])
    plt.gca().set_aspect('equal', 'datalim')



    def update(val):
        time_line_handle.set_xdata([val, val])
        new_pos = nearest_position(val)
        curr_pos_line_handle.set_xdata([new_pos[0]])
        curr_pos_line_handle.set_ydata([new_pos[1]])

        for log in logs:
            pred_line_handle = prediction_line_handles[log.name]

            pred_i = nearest_time_index(val)

            if pred_i < len(log.predictions)-1:
                pred = log.predictions[pred_i]
                pred_x_axis, pred_y_axis = gen_prediction_axis(pred)
                pred_line_handle.set_xdata(pred_x_axis)
                pred_line_handle.set_ydata(pred_y_axis)


    subplot_3 = plt.subplot(gs[2])
    plt.title("Time Slider", fontsize=16)
    time_slider = Slider(plt.gca(), '', error_bounds[0], error_bounds[1], color='grey')
    time_slider.on_changed(update)

    # subplot_4 = plt.subplot(gs[3])
    # RadioButtons(plt.gca(), ('red', 'blue', 'green'), active=0)

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
                        default=40000,
                        help='The upper error bound (for display)')
    parser.add_argument('-e', '--error-calc',
                        metavar='Method',
                        dest='error_calc_method',
                        nargs='?',
                        choices=error_calc_choices,
                        default=error_calc_choices[2],
                        help="Method for error calculation. Allowed values: " +
                        ", ".join(error_calc_choices))
    parser.add_argument('files',
                        metavar='File',
                        type=str,
                        nargs='+',
                        help="Files for analysis/comparison")

    args = parser.parse_args()
    run(args)
