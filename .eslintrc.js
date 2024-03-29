/**
 * File: /.eslintrc.js
 * Project: @promanager/keycloak-avatar-client
 * File Created: 31-07-2022 14:36:36
 * Author: Clay Risser
 * -----
 * Last Modified: 22-09-2022 10:57:18
 * Modified By: Clay Risser
 * -----
 * Pro Manager LLC (c) Copyright 2022
 */

const fs = require("fs");

const cspell = JSON.parse(fs.readFileSync(".vscode/settings.json").toString())[
  "cSpell.words"
];

module.exports = {
  extends: ["airbnb-typescript/base", "prettier"],
  parser: "@typescript-eslint/parser",
  root: true,
  env: {
    browser: true,
  },
  plugins: ["spellcheck", "import"],
  parserOptions: {
    project: ["./tsconfig.json"],
    ecmaFeatures: {
      legacyDecorators: true,
    },
  },
  rules: {
    "@typescript-eslint/comma-dangle": [
      "error",
      {
        arrays: "always-multiline",
        enums: "always-multiline",
        exports: "always-multiline",
        functions: "never",
        imports: "always-multiline",
        objects: "always-multiline",
      },
    ],
    "@typescript-eslint/indent": "off",
    "@typescript-eslint/no-shadow": "off",
    "@typescript-eslint/no-use-before-define": "off",
    "@typescript-eslint/no-redeclare": "off",
    "arrow-body-style": "off",
    "class-methods-use-this": "off",
    "comma-dangle": "off",
    "default-case": "off",
    "import/extensions": ["error", "never", { json: "always" }],
    "import/no-cycle": "off",
    "import/prefer-default-export": "off",
    "max-classes-per-file": "off",
    "max-lines": ["error", 999],
    "max-lines-per-function": ["warn", 999],
    "no-await-in-loop": "off",
    "no-empty-function": ["warn", { allow: ["constructors"] }],
    "no-extra-boolean-cast": "off",
    "no-inner-declarations": "off",
    "no-param-reassign": "off",
    "no-plusplus": "off",
    "no-return-assign": "off",
    "no-shadow": "off",
    "no-underscore-dangle": "off",
    "no-use-before-define": "off",
    "no-useless-constructor": "off",
    "react/jsx-props-no-spreading": "off",
    yoda: "off",
    "spellcheck/spell-checker": [
      "warn",
      {
        comments: true,
        strings: true,
        identifiers: true,
        lang: "en_US",
        skipWords: cspell,
        skipIfMatch: ["http?://[^s]*", "^[-\\w]+/[-\\w\\.]+$"],
        skipWordIfMatch: [],
        minLength: 3,
      },
    ],
    "lines-between-class-members": [
      "error",
      "always",
      { exceptAfterSingleLine: true },
    ],
    "import/no-unresolved": [
      "error",
      {
        ignore: ["^~"],
      },
    ],
    "no-unused-vars": [
      "warn",
      {
        args: "after-used",
        argsIgnorePattern: "^_",
        ignoreRestSiblings: true,
        vars: "all",
      },
    ],
    "@typescript-eslint/lines-between-class-members": [
      "error",
      "always",
      { exceptAfterSingleLine: true },
    ],
    "@typescript-eslint/no-unused-vars": [
      "warn",
      {
        args: "after-used",
        argsIgnorePattern: "^_",
        ignoreRestSiblings: true,
        vars: "all",
      },
    ],
    "import/no-extraneous-dependencies": [
      "error",
      {
        devDependencies: [
          "**/*.spec.js",
          "**/*.spec.jsx",
          "**/*.spec.ts",
          "**/*.spec.tsx",
          "**/*.test.js",
          "**/*.test.jsx",
          "**/*.test.ts",
          "**/*.test.tsx",
          "example/**/*",
          "tests/**/*.js",
          "tests/**/*.jsx",
          "tests/**/*.ts",
          "tests/**/*.tsx",
        ],
      },
    ],
  },
  overrides: [
    {
      files: ["*.test.js", "*.test.jsx", "*.test.ts", "*.test.tsx"],
      env: {
        jest: true,
      },
      plugins: ["jest"],
    },
    {
      files: ["*.ts", "*.tsx"],
      rules: {
        "no-unused-vars": "off",
      },
    },
  ],
  settings: {
    "import/resolver": {
      node: {
        extensions: [".js", ".jsx", ".ts", ".tsx"],
      },
    },
  },
};
