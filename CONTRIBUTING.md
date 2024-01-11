# Welcome to Accesso SDK for Android Contribution Guide

This guide will help you understand the general organization of the project and direct you to the best places to start contributing. 

## Releases
[![Release](https://github.com/accesso/accesso-sdk-android/actions/workflows/release.yml/badge.svg?branch=dev)](https://github.com/accesso/accesso-sdk-android/actions/workflows/release.yml)

## Pre-Releases
[![Pre-Release](https://github.com/accesso/accesso-sdk-android/actions/workflows/release.yml/badge.svg?branch=dev)](https://github.com/accesso/accesso-sdk-android/actions/workflows/release.yml)

## Prerequisites

You will need access to Jira, Confluence, and Slack to access the below links.

## Commandments

Please read the [commandments](https://accesso.atlassian.net/wiki/spaces/MDK/pages/69570494517/MSDK+Commandments) that every SDK developer should follow.

## Getting started

The mobile SDK has several modules that are responsible for specific functions. To get an overview of the SDK modules and their features, visit [accesso SDK Modules](https://accesso.atlassian.net/wiki/spaces/MDK/pages/69358649628/accesso+SDK+Modules).

We also have a [developer portal](https://accessodevelopmentkit.com/) where you can find all the technical documentation.

The credentials are pinned in the [#mobile-sdk-team channel](https://accessodev.slack.com/archives/C04FX87RF4Y/p1682027410358319?thread_ts=1682017486.098989&cid=C04FX87RF4Y). 

## Set up the development environment.

Download the project 

```bash
$ git clone https://github.com/accesso/accesso-sdk-android.git
```

Open it from the [Android Studio](https://developer.android.com/studio).

## How to contribute

### Sprint Stories

Select the unassigned user stories that are next in priority in the [Jira dashboard](https://accesso.atlassian.net/jira/software/c/projects/MSDK/boards/636). 
We follow an Agile methodology where the product team members define the new features that the SDK will have, and in collaboration with the development team, we refine the stories we will work on in the next sprint. You can find more information about the [life cycle of a story](https://accesso.atlassian.net/wiki/spaces/MDK/pages/69430150012/Mobile+SDK+General+Sprint+Story+Creation+Process).

### Sending a Pull Request

Here is a summary of the steps to follow:

1. Create a new topic branch (off the development branch) to contain your feature, change, or fix:

```bash
$ git checkout -b <msdk-ticket-id>
```

2. Make your code changes
   
3. Push your topic branch up:

```bash
$ git push origin <msdk-ticket-id>
```

4. [Open a Pull Request](https://help.github.com/articles/creating-a-pull-request/#creating-the-pull-request) with a clear title and description, using angular commit message conventions for correct [semantic versioning](https://accesso.atlassian.net/wiki/spaces/MDK/pages/69327552614/Semantic+Releases+Versioning+Commitlint+Oh+My).

### Static Code Analysis

All PRs go through [Static Code Analysis](https://accesso.atlassian.net/wiki/spaces/MDK/pages/69465539248/Static+Code+Analysis); if they do not meet the minimum requirements, the build will fail, and the PR can only be integrated once it does.

### Documentation

When integrating the PR, the technical documentation is generated, so we invite you to ensure that all new or modified code is documented properly according to the following [guide](https://accesso.atlassian.net/wiki/spaces/MDK/pages/69307105534/KDoc+and+Dokka+for+Android).
