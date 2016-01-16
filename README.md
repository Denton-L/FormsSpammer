# FormsSpammer

## Description
This project was written over a few hours out of complete boredom. It spams a Google Form with a bunch of random responses, taking advantage of the fact that there is no way to implement a CAPTCHA in Google Forms.

This project has many components to it. First of all, regex is used to parse for relevant elements. Although HTML is [not a regular language](https://stackoverflow.com/questions/1732348/regex-match-open-tags-except-xhtml-self-contained-tags), the elements contained within the `<form>` tag are regular and thus can be parsed.

After parsing, randomly generated options and text are generated and then submitted via POST request. Multithreading is used in order to speed up the spamming process.

## Improvements
* Add support for multi-page forms.
* Add support for new style of forms.
* Add a GUI.
* Check if form incorporates error checking and generate "smarter" submissions.
