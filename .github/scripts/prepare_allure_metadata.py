#!/usr/bin/env python3
"""Record the branch and GitHub run id before the Allure report is generated."""

import json
import os
import re
import sys
from pathlib import Path

results_dir = Path(os.environ.get("ALLURE_RESULTS", "target/allure-results"))
branch = os.environ.get("GITHUB_HEAD_REF") or os.environ.get("GITHUB_REF_NAME") or "local"
run_id = os.environ.get("GITHUB_RUN_ID", "local")
run_number = os.environ.get("GITHUB_RUN_NUMBER", "")
server = os.environ.get("GITHUB_SERVER_URL", "https://github.com").rstrip("/")
repository = os.environ.get("GITHUB_REPOSITORY", "")
owner = os.environ.get("GITHUB_REPOSITORY_OWNER", "")
repo_name = repository.split("/", 1)[1] if "/" in repository else repository

safe_branch = re.sub(r"[^A-Za-z0-9._/-]", "-", branch)
safe_branch = re.sub(r"/+", "/", safe_branch).strip("/")
if not safe_branch or ".." in safe_branch.split("/"):
    sys.exit(f"Refusing unsafe branch name: {branch}")

report_url = f"https://{owner}.github.io/{repo_name}/{safe_branch}/{run_id}/"
build_url = f"{server}/{repository}/actions/runs/{run_id}" if repository else ""

if not results_dir.is_dir():
    sys.exit(f"Allure results were not created at {results_dir}")

(results_dir / "environment.properties").write_text(
    "\n".join(
        [
            f"Branch={branch}",
            f"RunId={run_id}",
            f"RunNumber={run_number}",
            f"ReportUrl={report_url}",
            "",
        ]
    ),
    encoding="utf-8",
)

executor = {
    "name": "GitHub Actions",
    "type": "github",
    "url": server,
    "buildOrder": int(run_id) if run_id.isdigit() else run_id,
    "buildName": branch,
    "buildUrl": build_url,
    "reportUrl": report_url,
    "reportName": f"{branch} #{run_id}",
}
(results_dir / "executor.json").write_text(json.dumps(executor, indent=2) + "\n", encoding="utf-8")

output_path = os.environ.get("GITHUB_OUTPUT")
if output_path:
    with open(output_path, "a", encoding="utf-8") as output:
        output.write(f"report_url={report_url}\n")
        output.write(f"safe_branch={safe_branch}\n")

print(report_url)
