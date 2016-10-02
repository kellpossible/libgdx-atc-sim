import argparse
import csv
import matplotlib.pyplot as plt
import sys


class AircraftState:
    def __init__(self, state_time, error):
        self.state_time = state_time
        self.error = error


class Prediction:
    def __init__(self, aircraft_id, prediction_time):
        self.aircraft_id = aircraft_id
        self.prediction_time = prediction_time
        self.states = []

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
        pass


class ErrorCalculator(object):
    def run(self, prediction):
        pass


class AverageErrorCalculator(ErrorCalculator):
    def run(self, prediction):
        return prediction.average_error()


class TotalErrorCalculator(ErrorCalculator):
    def run(self, prediction):
        return prediction.total_error()


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


def run(args):
    selected_aircraft_id = args.aircraft_id

    logs = []

    # read them
    for filename in args.files:
        log = ErrorLog(filename, AverageErrorCalculator())
        print("reading: " + filename)
        with open(filename, 'r') as csvfile:
            reader = csv.reader(csvfile, delimiter=',')
            for row in reader:
                aircraft_id = row[0]
                if selected_aircraft_id is None:
                    selected_aircraft_id = aircraft_id

                if aircraft_id == selected_aircraft_id:
                    prediction_time = int(row[1])

                    prediction = Prediction(aircraft_id, prediction_time)

                    i = 1
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

        line, = plt.plot(x_axis, y_axis, label=log.name)
        line_handles.append(line)

    plt.axis([bounds[0], bounds[1], 0, 10000])
    plt.legend(handles=line_handles)
    plt.show()



if __name__ == "__main__":
    parser = argparse.ArgumentParser(
        description="""Compare integration test results.""",
        epilog="""Notes: Default aircraft for analysis is the first that appears.
        """)
    parser.add_argument('-a', '--aircraft-id', type=str, dest='aircraft_id',
                        metavar='ID',
                        nargs='?',
                        help='id of aircraft you want to select for analysis')
    parser.add_argument('files',
                        metavar='File',
                        type=str,
                        nargs='+',
                        help='files for analysis/comparison')

    args = parser.parse_args()
    run(args)
