#!/usr/bin/env python3
"""Publish one Allure report at <branch>/<run-id>/ without removing older reports."""

import os
import shutil
import subprocess
import sys
from pathlib import Path

safe_branch = os.environ.get("SAFE_BRANCH", "").strip("/")
run_id = os.environ["GITHUB_RUN_ID"]
if not safe_branch or ".." in safe_branch.split("/"):
    sys.exit("Refusing to publish without a safe branch name")
report_dir = Path(os.environ.get("ALLURE_REPORT", "target/site/allure-maven-plugin"))
publish = Path(os.environ.get("PAGES_DIR", "site-publish"))
repository = os.environ["GITHUB_REPOSITORY"]
token = os.environ["GITHUB_TOKEN"]
remote = f"https://github.com/{repository}.git"
history_branch = "allure-history"

if not (report_dir / "index.html").is_file():
    sys.exit(f"Allure report was not generated at {report_dir}")

if publish.exists():
    shutil.rmtree(publish)

clone = subprocess.run(
    [
        "git",
        "-c",
        f"http.extraheader=AUTHORIZATION: bearer {token}",
        "clone",
        "--depth",
        "1",
        "--branch",
        history_branch,
        remote,
        str(publish),
    ],
    capture_output=True,
    text=True,
)
if clone.returncode != 0:
    message = f"{clone.stderr}\n{clone.stdout}".lower()
    missing_branch = "not found" in message or "couldn't find remote ref" in message
    if not missing_branch:
        sys.stderr.write(clone.stderr)
        sys.exit(clone.returncode)
    if publish.exists():
        shutil.rmtree(publish)
    publish.mkdir(parents=True)
    subprocess.run(["git", "init"], cwd=publish, check=True)
    subprocess.run(["git", "checkout", "-b", history_branch], cwd=publish, check=True)

destination = publish.joinpath(*safe_branch.split("/"), run_id)
if destination.exists():
    shutil.rmtree(destination)
destination.mkdir(parents=True)
for item in report_dir.iterdir():
    target = destination / item.name
    if item.is_dir():
        shutil.copytree(item, target)
    else:
        shutil.copy2(item, target)

(publish / ".nojekyll").write_text("", encoding="utf-8")

reports = sorted(
    (
        index.parent.relative_to(publish).as_posix()
        for index in publish.rglob("index.html")
        if index.parent != publish
    ),
    reverse=True,
)
items = "\n".join(f'<li><a href="{path}/">{path}</a></li>' for path in reports)
(publish / "index.html").write_text(
    "\n".join(
        [
            "<!DOCTYPE html>",
            "<html>",
            "<head><meta charset=\"utf-8\"><title>Allure reports</title></head>",
            "<body>",
            "<h1>Allure reports</h1>",
            "<ul>",
            items,
            "</ul>",
            "</body>",
            "</html>",
            "",
        ]
    ),
    encoding="utf-8",
)

git = ["git", "-C", str(publish)]
subprocess.run([*git, "add", "-A"], check=True)
if subprocess.run([*git, "diff", "--cached", "--quiet"]).returncode != 0:
    subprocess.run(
        [
            *git,
            "-c",
            "user.email=41898282+github-actions[bot]@users.noreply.github.com",
            "-c",
            "user.name=github-actions[bot]",
            "commit",
            "-m",
            f"Allure report {safe_branch} {run_id}",
        ],
        check=True,
    )
    subprocess.run(
        [
            *git,
            "-c",
            f"http.extraheader=AUTHORIZATION: bearer {token}",
            "push",
            remote,
            f"HEAD:{history_branch}",
        ],
        check=True,
    )

git_dir = publish / ".git"
if git_dir.exists():
    shutil.rmtree(git_dir)

output_path = os.environ.get("GITHUB_OUTPUT")
if output_path:
    with open(output_path, "a", encoding="utf-8") as output:
        output.write("ready=true\n")
