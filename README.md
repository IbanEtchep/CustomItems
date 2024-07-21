# CustomItems

Ce plugin permet de créer des items ayant des capacités spéciales grâce à des attributs.

## Attributs:

| Attribut                    | Description                                                                                       | Valeurs                     |
|-----------------------------|---------------------------------------------------------------------------------------------------|-----------------------------|
| **HARVEST_REPLANT**         | Permet de replanter automatiquement les cultures                                                  |                             |
| **FERTILIZE**               | Permet de fertiliser les cultures en effectuant un clic droit, en échange de durabilité de l'item |                             |
| **RANGE_FERTILIZE**         | Permet de fertiliser les cultures dans un diamètre de 3 blocs                                     |                             |
| **RANGE_HARVEST**           | Permet de récolter les cultures dans un diamètre de 3 blocs                                       |                             |
| **RANGE_MINING**            | Permet de miner les blocs dans un diamètre de 3 blocs                                             |                             |
| **TREE_CUT**                | Permet de couper les arbres en cassant leur tronc                                                 |                             |
| **MELT_MINING**             | Fait fondre automatiquement les blocs minés                                                       |                             |
| **POWER_BOOTS**             | Permet de donner un effet de double saut aux bottes                                               |                             |
| **UNREPAIRABLE_BY_COMMAND** | Empêche de réparer l'item par commande /repair                                                    |                             |
| **UNREPAIRABLE_BY_ANVIL**   | Empêche de réparer l'item par enclume                                                             |                             |
| **UNREPAIRABLE_BY_MENDING** | Empêche de réparer l'item par mending                                                             |                             |
| **ANIMAL_CATCHER**          | Permet de capturer les animaux en effectuant un clic droit et les relâcher ailleurs               |                             |
| **VILLAGER_CATCHER**        | Permet de capturer les villageois en effectuant un clic droit et les relâcher ailleurs            |                             |
| **REQUIRE_JOB_LEVEL**       | Permet de définir un niveau de job requis pour utiliser l'item                                    | **level(int), job(String)** |

## Commandes:

| Commande                                                      | Description                                                  |
|---------------------------------------------------------------|--------------------------------------------------------------|
| **/customattribute list**                                     | Permet de lister les attributs sur l'item en main            |
| **/customattribute add <attribut>**                           | Permet d'ajouter un attribut à l'item en main                |
| **/customattribute remove <attribut>**                        | Permet de retirer un attribut à l'item en main               |
| **/customattribute addValue <attribut> <cléValeur> <valeur>** | Permet d'ajouter une valeur à un attribut de l'item en main  |
| **/customattribute removeValue <attribut> <cléValeur>**       | Permet de retirer une valeur à un attribut de l'item en main |

**Exemple REQUIRE_JOB_LEVEL:**

- /customattribute add REQUIRE_JOB_LEVEL
- /customattribute addValue REQUIRE_JOB_LEVEL level 10
- /customattribute addValue REQUIRE_JOB_LEVEL job Mineur