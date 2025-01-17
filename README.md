# [amigoda](https://github.com/amigoda) / [poc-github-actions](https://github.com/amigoda/poc-github-actions)

The purpose of this repository is to test [**GitHub Actions**](https://github.com/features/actions),
a continuous integration and continuous delivery (CI/CD) platform that allows you to automate building, testing,
and deploying pipelines.

This repository contains is a small Java Spring Boot web application that has a **REST API** that returns the
date and time of the server. It also contains some unit tests to verify that they are executed when a Graddle build
is run.


## About GitHub Actions

[**GitHub Actions**](https://github.com/features/actions) is a framework to Automate the Git workflow
from the idea to production. It is similar to
[Gitlab CI](https://docs.gitlab.com/ee/ci/) or
[Jenkins](https://www.jenkins.io/).

### Definitions

#### Workflows

A **workflow** is a **YAML file** that contains a configurable automated process consisting of one or more **jobs**.
The workflow YAML files are created in the `.github/workflows` folder.
A repository can have multiple workflows (i.e. multiple YAML files), each of which can perform a different set of tasks.

Each workflow has one or more execution conditions, and you can specify the **events** that trigger it
(push, merge, fork, ...) and many other parameters such as the branches or tags affected.

#### Events

An **event** is a specific activity in the Git repository that **triggers a workflow run**.

There are many events that can trigger a workflow.
The most common events are `push` and `pull_request` but there are tens of different events:
* `create` when a new branch or tah is created.
* `delete` when a new branch or tah is deleted.
* `deployment` when a deployment is created.
* `fork` when the repository is forked.
* `gollum` when the Wiki page is created or updated.
* `issues` when an issue is created or modified.
* `label` when label is created or modified.
* `pull_request` when some activity on a pull request occurs.
* `push` when a commit or tag is pushed.
* `release` when release activity occurs.
* `schedule` to trigger a workflow at a scheduled time (like a cron job).
* ...and many more. See [events that trigger workflows](https://docs.github.com/en/actions/writing-workflows/choosing-when-your-workflow-runs/events-that-trigger-workflows) page.

#### Jobs and Steps

A **job** is a set of **steps** in a workflow that is executed on the same **runner**,
so you can share data from one step to another.
Steps are executed in sequence within the job.
If a step fails, the entire job fails and the rest of the steps are not executed.

A step is either a set of shell commands, or an **action**.

You can configure a job's **dependencies** with other jobs.
However the jobs that do not have dependencies run in parallel.
When a job has a dependency on another job,
it waits for the dependent job to complete before running.

#### Actions

An **action** is a predefined, complex but frequently repeated task.
There are some official actions like
[`actions/checkout`](https://github.com/actions/checkout),
[`actions/setup-java`](https://github.com/actions/setup-java),
[`actions/setup-node`](https://github.com/actions/setup-node),
[`actions/upload-artifact`](https://github.com/actions/upload-artifact).
You can also write your own actions.
See a [curated list of awesome actions](https://github.com/sdras/awesome-actions?tab=readme-ov-file).


#### Dependencies

The **dependencies** between jobs are defined in the YAML, under the label `jobs.<job_id>.needs`.

```yaml
jobs:
  job1:
  job2:
    needs: job1
  job3:
    needs: [ job1, job2 ]
```

The execution order in this case is `job1`, then `job2`, and then `job3`.

#### Runners

A **runner** is a **server** that runs the workflow.
It can be an Ubuntu Linux, a Microsoft Windows, or a macOS virtual machine.
Only one job can be run at a time in a server.

### Example of a workflow

This is a simple workflow that runs a single job with many steps on push events.

```yaml
name: Run Gradle Tests

run-name: Run Graddle Tests on ${{ github.repository }}

on: [ push ]

jobs:
  Run-Gradle-Tests:
    runs-on: ubuntu-latest
    steps:
      - run: |
          echo "Repository is ${{ github.repository }}."
          echo "Branch is ${{ github.ref }}."
          echo "User is ${{ github.actor }}."
          echo "Event is ${{ github.event_name }}."

      - name: Check out repository ${{ github.repository }}
        uses: actions/checkout@v4

      - name: Set up JDK 21
        uses: actions/setup-java@v4
        with:
          java-version: '21'
          distribution: 'temurin'
          architecture: 'x64'
          server-id: 'github'
          settings-path: ${{ github.workspace }}

      - name: Build with Gradle
        run: |
          cd ${{ github.workspace }}
          ./gradlew build

      - name: Test with Gradle
        run: |
          ./gradlew test

      - name: Ensure app is compiled
        run: ls ${{ github.workspace }}/build/libs/

      - run: echo "Job finished. The status is ${{ job.status }}."
```

### Links

* [GitHub Actions documentation](https://docs.github.com/en/actions)
* [Quickstart for GitHub Actions](https://docs.github.com/en/actions/writing-workflows/quickstart)
* [Understanding GitHub Actions](https://docs.github.com/en/actions/about-github-actions/understanding-github-actions).
* [Workflow syntax for GitHub Actions](https://docs.github.com/en/actions/writing-workflows/workflow-syntax-for-github-actions).

