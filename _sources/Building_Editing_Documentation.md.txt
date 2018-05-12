# Building and Editing the Documentation

_Section author: Luke Frisken <[l.frisken@gmail.com](mailto:l.frisken@gmail.com)>_


**Table of Contents**

<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [Project Documentation](#project-documentation)
- [Introduction](#introduction)
- [Building the Documentation](#building-the-documentation)
  - [Pre-Requisites](#pre-requisites)
    - [Linux](#linux)
    - [Windows](#windows)
    - [All](#all)
  - [Server Autobuild](#server-autobuild)
  - [Clean](#clean)
  - [Deploy](#deploy)
- [RestructuredText Support](#restructuredtext-support)
- [Markdown Support](#markdown-support)
  - [Markdown Syntax](#markdown-syntax)
  - [Markdown Tools](#markdown-tools)
  - [Extra Markdown Features](#extra-markdown-features)
- [UML](#uml)
  - [Dependency Diagram Generation](#dependency-diagram-generation)

<!-- /TOC -->

## Introduction


This project uses [Sphinx](http://www.sphinx-doc.org/en/stable/) with the [Read The Docs Theme](http://readthedocs.org) for building this set of documentation. A number of other tools are also used to automatically generate API documentation and UML Diagrams.

Another good overview of the technologies used here is available at
[Wiser Readthedocs](http://build-me-the-docs-please.readthedocs.io/en/latest/index.html)

## Building the Documentation

### Pre-Requisites

#### Linux

following packages need to be installed:

 + lxml2-dev
 + graphviz
 + java
 + pandoc

#### Windows

**WARNING!**
> Windows build is currently unsupported/experimental as of the latest changes,
> it was not working on the team member's computer who had windows, and I do
> not have time to install a windows VM to fix it myself. - Luke Frisken

Following the installation section labelled "All" found below is the general guideline for the Windows installation, however there are a few considerations that need to be made.

+ Install Pandoc. [Link](http://pandoc.org/installing.html)
+ The lxml package must be installed manually before installing javasphinx, find the corresponding lxml version by matching the python3 version you have installed. [Link](http://www.lfd.uci.edu/~gohlke/pythonlibs/#lxml)

#### All

A number of things need to be installed on your computer before attempting to build the documentation.

Firstly [Python](https://www.python.org/), preferably **Python 3** needs to be installed. Python can be obtained from: [https://www.python.org/downloads/](https://www.python.org/downloads/)

The following Python modules also need to be installed:
 + [sphinx_rtd_theme](https://pypi.python.org/pypi/sphinx_rtd_theme) - A theme for sphinx
 + [recommonmark](https://recommonmark.readthedocs.org/en/latest/) - A module required for parsing markdown files
 + [javasphinx](http://bronto.github.io/javasphinx/) - A module required for generating java documentation from java files
 + [sphinxcontrib-plantuml](https://pypi.python.org/pypi/sphinxcontrib-plantuml) - A module required for displaying [PlantUML](http://plantuml.com/) diagrams in documetation
 + [pygments-markdown-lexer](https://github.com/jhermann/pygments-markdown-lexer) - A module required for using Pygments to give syntax highlighting to markdown documents
 + [nbsphinx](https://nbsphinx.readthedocs.io/) -  A module required to render jupyter notebooks

The required python modules can all be installed with the following command run in the documentation folder:

    $ pip install -r requirements.txt

Included in the git repo for your convenience, but also by definition, required are:
 + [PlantUML](http://plantuml.com/) (plantuml.jar) - For rendering UML diagrams
 + [PlantUML Dependency](http://plantuml-depend.sourceforge.net/) - For generating PlantUML class dependency diagrams from java source code

### Build

To build the documentation, run this command from the documentation folder:

	$ make html

The documentation can then be found in the *\_build/html* folder.


### Autobuild

If you'd like documentation to automatically build upon changes to its source files, and to show in the browser, there is a fantastic python module called [sphinx-autobuild](https://pypi.python.org/pypi/sphinx-autobuild). It can be installed using:

	$ pip install sphinx-autobuild

You can then run:

	$ make livehtml

which will open up the documentation in a live browser view.

See the [python package index page](https://pypi.python.org/pypi/sphinx-autobuild) for more information on how to use.

### Clean

Occasionally the cache gets a little messed up and items can go missing from the index or toolbar. In this case you can clean the build to allow the documenation to be rebuilt from scratch with this command:

	$ make clean

### Deploy

To deploy the documentation, I'm using this method to publish docs to a gh-pages branch on github: https://gist.github.com/ramnathv/2227408

## RestructuredText Support

Sphinx's default documenation markup is called reStructuredText. A good overview
of the syntax is available [here](http://build-me-the-docs-please.readthedocs.io/en/latest/Using_Sphinx/OnReStructuredText.html) and [here](http://rest-sphinx-memo.readthedocs.io/en/latest/ReST.html).
A more formal specification of the syntax is available [here](http://docutils.sourceforge.net/docs/user/rst/quickref.html)

### External Links
Currently using [extlinks](http://www.sphinx-doc.org/en/stable/ext/extlinks.html) we
have several external link shortcuts set up:

**Issues:**

	:issue:`24`

**Commits:**

	:commit:`7862861e42fd628ee701447164137ef3f9bc6ca5`

**Source Files in the Git Repo:**

	:sourcefile:`sourcefile/`

## Markdown Support

This project's documentation supports the Sphinx default markup language of [reStructuredText](#restructuredtext-support) with .rst files, but it also supports Markdown with .md files. The preferred language for use in this documentation is Markdown, although reStructuredText statements can be inserted into the Markdown using a special method when neccessary.

Taken from John Gruber’s [website](http://daringfireball.net/projects/markdown/syntax#philosophy):

> _Markdown is intended to be as easy-to-read and easy-to-write as is feasible._
>
> _Readability, however, is emphasized above all else. A Markdown-formatted document should be publishable as-is, as plain text, without looking like it’s been marked up with tags or formatting instructions. While Markdown’s syntax has been influenced by several existing text-to-HTML filters — including Setext, atx, Textile, reStructuredText, Grutatext, and EtText — the single biggest source of inspiration for Markdown’s syntax is the format of plain text email._
>
>_To this end, Markdown’s syntax is comprised entirely of punctuation characters, which punctuation characters have been carefully chosen so as to look like what they mean. E.g., asterisks around a word actually look like *emphasis*. Markdown lists look like, well, lists. Even blockquotes look like quoted passages of text, assuming you’ve ever used email._

### Markdown Syntax

A good overview of the syntax of markdown is available [here](http://daringfireball.net/projects/markdown/syntax), and more succinctly [here](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet)

A more formal specification of its syntax has now been provided called CommonMark, which can be found [here](http://spec.commonmark.org/)


### Markdown Tools

Markdown has fairly widespread support among the popular text editors. If you're using sublime a couple of tools can make your life easier:

 + [Markdown Extended Support for Sublime](https://github.com/jonschlinkert/sublime-markdown-extended) - better syntax highlighting
 + [Monakai Extended Theme](https://github.com/jonschlinkert/sublime-monokai-extended) - needed for above to take effect
 + [Omni Markup Previewer](https://github.com/timonwong/OmniMarkupPreviewer) - live markdown preview in browser


### Extra Markdown Features

The [AutoStructify](https://recommonmark.readthedocs.org/en/latest/auto_structify.html) module for [ReCommonMark](https://recommonmark.readthedocs.org) is in use for this documentation to allow extra features in markdown for use in Sphinx documentation. These features include:

 + [toctree support](https://recommonmark.readthedocs.org/en/latest/auto_structify.html#auto-toc-tree)
 + [References to other documents](https://recommonmark.readthedocs.org/en/latest/auto_structify.html#auto-doc-ref)
 + [Url resolving for references to files in the git repository](https://recommonmark.readthedocs.org/en/latest/auto_structify.html#url-resolver) - [example here](../Libgdx_ATC_Simulator/core/src/com/atc/simulator/ATCSimulator.java)
 + [Code blocks with syntax highlighting](https://recommonmark.readthedocs.org/en/latest/auto_structify.html#codeblock-extensions) - see [here](https://help.github.com/articles/creating-and-highlighting-code-blocks/#syntax-highlighting) for more details on the syntax and supported languages. List of languages and their names [here](http://pygments.org/docs/lexers/)
 + [Latex math formulas](https://recommonmark.readthedocs.org/en/latest/auto_structify.html#math-formula)
 + [Embedded reStructuredText](https://recommonmark.readthedocs.org/en/latest/auto_structify.html#embed-restructuredtext) - very useful for sphinx rst commands


## UML

We are using [PlantUML](http://plantuml.com/) for our UML diagramming. Diagrams are defined using a simple language ([see PlantUML Language Reference Guide](http://plantuml.com/PlantUML_Language_Reference_Guide.pdf)). Diagrams are inserted into rst/markdown files in Sphinx as svg images.

The command to generate an svg image is:

	$ plantuml -tsvg diagram.puml

The reStructuredText syntax for inserting this image as a figure is:

	.. figure:: diagram.svg
		:width: 100 %
		:align: center

		Caption for diagram

### Dependency Diagram Generation
You can use [PlantUML Dependency](http://plantuml-depend.sourceforge.net/) like this to generate a dependency diagram for source code. The contents can then be copied into a markdown file:

	java -jar ./tools/plantuml-dependency-cli-1.4.0-jar-with-dependencies.jar -b . -i **/*.java -o atc_simulator.puml -dp ^.*com[.]atc[.]simulator.*$


An example of the diagram generated with this command can be found in [Libgdx ATC Simulator](Libgdx_ATC_Simulator.md) section of the documentation.
