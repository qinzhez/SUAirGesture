# Features

## Speed (feature 1~9)
- Calculate 3 types of speed: Euclid, x of Manhattan, y of Manhattan
- 3 types of calculation: Max, Min and Average
- The distance used for speed is standardized by using: standardized distance = Real distance / (max real distance in all frames)

## Distance (feature 10, )
- Get a central guide line of an axis, calculate standard deviation (feature 10)
- Separate horizontally into several areas, get ratio of x and y inside each area (for example 5 areas, feature 11~15)
- Weighted drift of a central guide line for each point. Then, sum them up. The weight is the ratio of current drift to max axis interval (or average interval). Score will represent large change score and small change score (feature 16,17,18,19)

## Angle (feature)