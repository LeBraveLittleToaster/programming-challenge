# Development Notes

## Task - Weather

- One line is one day
- Day number should be the output
- minimum between max (mxT) and min (MnT)

## Task - Football

- Contains results
- "Goals" and "Goal Allowed"
- search for lowest difference between goals and goals allowed


## Task - Before designing approach

- Without analysis, multiple "smallest differences" can occur in both datasets
- Reading the file is a concern
  - Input can be empty
  - Decide on file ending for appropriate strategy
  - Filepath absolute and outside of resources or shipped with the application?
- Checking/Cleaning the data is a concern, maybe with different strategies
    - Empty entries
    - Non-numeric entries
    - Header not present
    - Columns not present
    - Negativ points
- Output is a concern, maybe an internal, intermediate data format
- Testing is a concern, reproducibility of test results, potentially scramble the data in testing
