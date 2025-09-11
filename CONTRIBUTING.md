# Contributing Guidelines

Thank you for considering contributing to this project! 🎉  
We welcome contributions of all kinds — from bug fixes and documentation improvements to new features.  
This document explains how you can help, the workflow to follow, and the standards we expect.


## 📌 Before You Start

- Please read the [README.md](./README.md) to understand the purpose, setup, and architecture of the project.
- Make sure you are familiar with **Java**, **Selenium**, **Cucumber**, and **TestNG** basics.
- Check the [issues](../../issues) to see if your idea, feature request, or bug has already been reported.
- Be respectful and constructive. Contributions should make the project better for everyone.



## 🛠 Types of Contributions

You can contribute in multiple ways:

- **Bug Fixes** → Fix errors in existing classes or utilities.
- **New Features** → Add helper methods, page utilities, or improve Docker/Grid support.
- **Tests** → Increase coverage, write new scenarios, or improve step definitions.
- **Documentation** → Update README, add setup instructions, or fix typos.
- **Refactor** → Simplify code, improve performance, or clean up duplication.
- **CI/CD** → Add GitHub Actions, Jenkins, or other automation scripts.



## 🔀 Workflow

1. **Fork** the repository to your GitHub account.
2. **Clone** your fork locally:
```bash
git clone https://github.com/metin-5115/Selenium-substructure.git

cd Selenium-substructure

git remote add upstream https://github.com/metin-5115/Selenium-substructure.git   # Add original repo as 'upstream'

git fetch upstream                                                                # Fetch latest changes from upstream

git checkout main                                                                 # Switch to main branch

git merge upstream/main                                                           # Merge upstream changes into your local main

git checkout -b feature/yeni-ozellik   # Create and switch to a new branch  example :feature, fix, refactor, docs

mvn clean test   # Clean and run all tests with Maven

git add .                                                # Stage all changes

git commit -m "Fix: your message"   # Commit with a clear message example :Add, Fix, Refactor, Docs

git push origin feature/yeni-ozellik
---