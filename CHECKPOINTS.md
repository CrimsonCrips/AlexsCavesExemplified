# Checkpoints

Each checkpoint is a snapshot of the whole working tree (tracked and untracked files) saved as `refs/checkpoints/vN`. Snapshots never touch branches, the index or the working tree.

Compare two checkpoints:

```bash
git diff --stat refs/checkpoints/v1 refs/checkpoints/v2
```

Compare a checkpoint with the current files:

```bash
git diff --stat refs/checkpoints/v1
```

Newest checkpoints are at the top.
