# AgriJsonUtilities
A set of tools to assist in the creation and generation of jsons for AgriCraft and AgriPlants.
Currently the following tools exist:
* Generate Recipe Jsons

## Generate Recipe Jsons
This tool allows to generate jsons from the AgriPlants repository based on templates in the resources.
These templates are json files which define how the AgriPlants json is converted into a recipe json.
Every template must contain at least a configuration entry:
```
"<agri_json_processor_config>": {
    "target": "plant",
    "directory": "botany_pots",
    "process": [
      "type",
      "conditions",
      "plant",
      "growthTicks",
      "growthStatFactor"
    ]
  }
```

This config entry must contain the following entries:
* target: defines the type of AgriPlants json which is targeted (plant, soil, weed, mutation, or fertilizer)
* directory: defines the output directory for the generated jsons
* process: defines all other entries in the json to process, the template json must contain an entry corresponding to each of the strings defined here, and an entry will be generated in the output jsons for these as well.

Thus, for each of the strings listed under 'process', one entry must be defined in the template json as well.
Entries are defined as follows: 
```
  "<entry_name>": {
    "type": "<processor_type>",
    <processor_arguments>
  },
```

### Program arguments
The Generate Recipe Jsons tool has two optional arguments:
* token: allows specifying a GitHub auth token to increase the number of daily API calls you can make (e.g. `token=<my_github_auth_token>`)
* modid: by default, the tool will apply the templates to all jsons for all mods in the AgriPlants repository, it is possible to limit this to a single mod by specifying the mod id (e.g. `modid=<target_mod_id>`)

### Processors
The following processors are available:

#### Copy
This processor simply copies the value of an AgriPlants json to the output json,
it requires a single argument, "value", which signals they key to fetch in the AgriPlants json.
```
"<entry_name>": {
    "type": "copy",
    "value": "<agriplants_json_entry_key>"
  },
```

#### Growth Stat Factor
This processor only applies to plant jsons, and requires no arguments.
It results in a double, which is the result of dividing the `"growth_bonus"` and `"growth_chance"` values.
```
  "<entry_name>": {
    "type": "growth_stat_factor"
  }
```

#### Growth Ticks
This processor only applies to plant jsons, and requires a single argument: `"base"`.
It results in an int which is the result of dividing the specified base value by the `"growth_chance"` value in the plant json.
It effectively converts the growth chance during random ticks, into a fixed amount of scheduled ticks.
```
  "<entry_name>": {
    "type": "growth_ticks",
    "base": "1200"
  }
```

#### Loaded Mod Conditions
This processor generates a Forge loaded mods recipe requirement condition,
it generates this condition for all dependencies specified in the AgriPlants json.
It also has one optional parameter, `"include"`, which takes an array of strings containing additional conditions to include.
Note that this processor will ignore the `"minecraft"` and `"agricraft"` keys as these are trivial.
```
  "<entry_name>": {
    "type": "loaded_mod_conditions",
    "include": ["<required_mod_id_1>", "<required_mod_id_2>, ..."]
  }
```

#### Plant ID
This processor does not require any parameters and simply injects the plant id specified in a plant json,
as a result, it can only be applied to plant jsons.
```
  "<entry_name>": {
    "type": "plant_id"
  }
```

### Examples
Example templates can be found under `resources/templates`.
