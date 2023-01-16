import matplotlib.pyplot as plt
import pandas as pd


def plot_one(file_path):
    # df = pd.read_csv("data/" + file_path)

    # read data from file and plot it
    # (index works out to be generation, so don't need to explicitly read generation in)
    df = pd.read_csv(file_path)
    a0 = df.iloc[:, 2]
    plt.plot(a0, label="average")
    a1 = df.iloc[:, 3]
    plt.plot(a1, label="best")
    a2 = df.iloc[:, 1]
    plt.plot(a2, label="worst")

    # labels and stuff
    plt.legend()
    # plt.title(file_path)
    plt.xlabel("generation")
    plt.ylabel("fitness")
    plt.ylim(-820, -180)
    # plt.gcf().canvas.manager.set_window_title(file_path)
    plt.show()


if __name__ == '__main__':

    file_path = "data.csv"
    plot_one(file_path)
