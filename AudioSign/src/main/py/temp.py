import numpy as np
import matplotlib.pyplot as plt

# Generate synthetic data (replace with real distances)
np.random.seed(0)

# Distances for matches
matches_distances = np.random.normal(loc=800, scale=100, size=1000)

# Distances for non-matches
non_matches_distances = np.random.normal(loc=1200, scale=100, size=1000)

# Plot histograms for matches and non-matches
plt.figure(figsize=(10, 6))

plt.hist(matches_distances, bins=30, alpha=0.5, color='blue', label='Matches')
plt.hist(non_matches_distances, bins=30, alpha=0.5, color='red', label='Non-Matches')

plt.axvline(x=1000, color='green', linestyle='dashed', linewidth=2, label='Threshold')

plt.xlabel('DTW Distance')
plt.ylabel('Frequency')
plt.title('Distribution of DTW Distances for Matches and Non-Matches')
plt.legend()
plt.grid(True)

plt.show()
