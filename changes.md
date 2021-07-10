# Changes to make
The following provides a list on everything that needs to be changed when using this as a template
- PLUGIN_NAME refers to the name of the plugin.
- PLUGIN_PACKAGE refers to the package the plugin is in (e.g. `me.scyphers.plugins.pluginname`)
- PLUGIN_VERSION refers to the desired version of the plugin

### .gitignore
- add `changes.md` to remove this file from version tracking

### build.gradle
- update `group` with PLUGIN_PACKAGE
- update `version` with PLUGIN_VERSION

### Plugin
- rename class and file to PLUGIN_NAME
- update method `getSplashText()` with splash text you want to provide

### plugin.yml
- update `main` with PLUGIN_PACKAGE and PLUGIN_NAME
- update `name` with PLUGIN_NAME
- update `version` with PLUGIN_VERSION
- update `authors` with all major contributors

### messages.yml
- update `prefix` with a coloured PLUGIN_NAME

### Misc
- Remove any unused classes that are not needed to reduce file size
- Remove `changes.md` once the list is complete
