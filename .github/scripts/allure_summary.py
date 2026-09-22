#!/usr/bin/env python3
"""Write one row per Allure test result into the GitHub Actions job summary."""

import json
import os
from pathlib import Path

results_dir = Path(os.environ.get("ALLURE_RESULTS", "target/allure-results"))
summary_path = os.environ.get("GITHUB_STEP_SUMMARY")
result_files = sorted(results_dir.glob("*-result.json"))

rows = []
for path in result_files:
    data = json.loads(path.read_text(encoding="utf-8"))
    labels = {label.get("name"): label.get("value", "") for label in data.get("labels", [])}
    start = data.get("start") or 0
    stop = data.get("stop") or start
    description = " ".join((data.get("description") or "").split())
    rows.append(
        {
            "suite": labels.get("suite") or labels.get("parentSuite") or "Suite",
            "name": data.get("name") or path.stem,
            "description": description,
            "status": data.get("status") or "unknown",
            "seconds": max(stop - start, 0) / 1000,
        }
    )

rows.sort(key=lambda row: (row["suite"], row["name"]))
counts = {}
for row in rows:
    counts[row["status"]] = counts.get(row["status"], 0) + 1


def cell(value):
    return str(value).replace("|", "\\|")


lines = ["## Test results", ""]
if not rows:
    lines.append("No Allure results were generated.")
else:
    totals = ", ".join(f"{status}: {count}" for status, count in sorted(counts.items()))
    lines.append(f"**{len(rows)} tests** — {totals}")
    lines.append("")
    lines.append("| # | Area | Test | Result | Time |")
    lines.append("|---:|---|---|---|---:|")
    for index, row in enumerate(rows, start=1):
        title = row["name"] if not row["description"] else f"{row['name']} — {row['description']}"
        lines.append(
            f"| {index} | {cell(row['suite'])} | {cell(title)} | {cell(row['status'])} | {row['seconds']:.1f}s |"
        )
    lines.extend(
        [
            "",
            "The full Allure report is attached to this run as the **allure-report** artifact.",
            "Open `index.html` inside it. Pushes to the default branch also publish that report to GitHub Pages.",
        ]
    )

text = "\n".join(lines) + "\n"
if summary_path:
    with open(summary_path, "a", encoding="utf-8") as summary:
        summary.write(text)
else:
    print(text)
