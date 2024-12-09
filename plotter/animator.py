import json

import matplotlib.animation as animation
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd


# Set up the course
def setup_course():
    fig, ax = plt.subplots(figsize=(24, 24))
    ax.set_xlim(0, 100)
    ax.set_ylim(0, 100)
    ax.set_xticks(np.arange(0, 101, 10))
    ax.set_yticks(np.arange(0, 101, 10))
    ax.grid(True)

    # Draw the course
    ax.plot(
        [0, 20, 20, 60, 60, 70, 70, 100],
        [45, 45, 20, 20, 80, 80, 45, 45],
        color="black",
        linewidth=2,
    )
    ax.plot(
        [0, 30, 30, 50, 50, 80, 80, 100],
        [55, 55, 30, 30, 90, 90, 55, 55],
        color="black",
        linewidth=2,
    )

    goals = [
        ([20, 20], [55, 45]),
        ([20, 30], [30, 30]),
        ([50, 50], [20, 30]),
        ([50, 60], [80, 80]),
        ([70, 70], [80, 90]),
        ([70, 80], [55, 55]),
        ([100, 100], [55, 45]),
    ]
    for goal in goals:
        x, y = goal
        ax.plot(x, y, color="cyan", linewidth=2, linestyle="dashed")
    # Optional blue points
    blue_points = [(25, 50), (25, 25), (55, 25), (55, 85), (75, 85), (75, 50)]
    for x, y in blue_points:
        ax.scatter(x, y, color="cyan", zorder=5)

    # Start and end markers
    ax.text(0, 49, "A", color="red", fontsize=12)
    ax.text(100, 49, "B", color="red", fontsize=12)

    return fig, ax


# Load particle data from a CSV file
def load_data(file_path):
    data = pd.read_csv(file_path)
    particles = []
    for i in range(10):  # Assuming 10 particles
        particles.append(
            data[["t", f"p{i}x", f"p{i}y", f"c{i}"]].rename(
                columns={f"p{i}x": "x", f"p{i}y": "y", f"c{i}": "color_flag"}
            )
        )
    return pd.concat(particles, ignore_index=True)


# Update the animation frame
def update(frame, circles, data):
    t_data = data[data["t"] == frame]
    for i, circle in enumerate(circles):
        particle_data = t_data.iloc[i]
        circle.center = (particle_data["x"], particle_data["y"])
        circle.set_facecolor("gray" if particle_data["color_flag"] else "blue")
    return circles


# Main function to create the animation
def animate_particles(file_path, output_path):
    data = load_data(file_path)
    fig, ax = setup_course()

    # Create circle objects for each particle
    circles = []
    json_file = json.load(open("../config.json"))
    for _ in range(json_file["N"]):  # Assuming 10 particles
        circle = plt.Circle((0, 0), 0.25, color="blue", zorder=5)
        ax.add_patch(circle)
        circles.append(circle)

    # Get unique time steps
    time_steps = data["t"].unique()

    # Create animation
    ani = animation.FuncAnimation(
        fig, update, frames=time_steps, fargs=(circles, data), interval=200, blit=True
    )

    # Save the animation
    ani.save(output_path, writer="ffmpeg", fps=24)  # Adjust fps as needed

    plt.show()


# Main execution point
if __name__ == "__main__":
    # Specify the path to your CSV file
    csv_file_path = "../race/output.csv"
    output_file_path = "animation.mp4"

    # Call the animation function
    animate_particles(csv_file_path, output_file_path)

