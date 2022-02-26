module.exports = {
    extends: ["@commitlint/config-conventional"],
    rules: {
        "subject-case": [2, "always", "sentence-case"],
        "header-max-length": [2, "always", 50],
        "body-max-line-length": [2, "always", 72]
    }
}
