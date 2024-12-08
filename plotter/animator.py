import matplotlib.animation as animation
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd


# Set up the course
def setup_course():
    fig, ax = plt.subplots(figsize=(8, 8))
    ax.set_xlim(0, 100)
    ax.set_ylim(0, 100)
    ax.set_xticks(np.arange(0, 101, 10))
    ax.set_yticks(np.arange(0, 101, 10))
    ax.grid(True)

    # Draw the course
    ax.plot(
        [10, 10, 30, 30, 50, 50, 70, 70, 90],
        [60, 40, 40, 30, 30, 50, 50, 60, 60],
        color="black",
        linewidth=2,
    )

    # Optional blue points
    # blue_points = [(10, 50), (30, 50), (30, 30), (50, 30), (50, 40), (70, 40)]
    # for x, y in blue_points:
    #     ax.scatter(x, y, color='blue', zorder=5)

    # Start and end markers
    ax.text(5, 58, "A", color="red", fontsize=12)
    ax.text(92, 58, "B", color="red", fontsize=12)

    return fig, ax


# Load particle data from a CSV file
def load_data(file_path):
    return pd.read_csv(file_path)


# Update the animation frame
def update(frame, scatter, data):
    t_data = data[data["t"] == frame]
    scatter.set_offsets(t_data[["pix", "piy"]].values)
    return (scatter,)


# Main function to create the animation
def animate_particles(file_path):
    data = load_data(file_path)
    fig, ax = setup_course()

    # Initial scatter plot for particles
    scatter = ax.scatter([], [], color="red", zorder=5)

    # Get unique time steps
    time_steps = data["t"].unique()

    # Create animation
    ani = animation.FuncAnimation(
        fig, update, frames=time_steps, fargs=(scatter, data), interval=200, blit=True
    )

    plt.show()


# Main execution point
if __name__ == "__main__":
    # Specify the path to your CSV file
    csv_file_path = "../race/output.csv"

    # Call the animation function
    animate_particles(csv_file_path)
