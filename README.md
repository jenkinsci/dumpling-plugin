dumpling-plugin
===============

Dumpling plugin for Jenkins CI brings [dumpling model and DSL](https://github.com/olivergondza/dumpling) to groovy console.

Plugin exposes `hudson.model.Dumpling` class to facilitate access. Install plugin and use dumpling as easily as:


    Dumpling.runtime.threads.grep { it.name.startsWith('Executor') }

