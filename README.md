Note 14/20

Les interfaces comme IPlayer ne doivent pas aller dans ihm mais dans un paquetage stable (comme logic dont le nom est d'ailleurs mal choisi)
IBoard est une interface inutile : la classe Board ne changera pas
les méthodes de IPlayer ne sont pas assez générales il ne faut pas obliger à découper en ligne et en colonne, il faut retourner un coup entier avec Coord par exempel



Diagramme d'architecture de l'application :

![archi](https://github.com/Yugenx/GOjava/assets/128364634/f15ee44d-e8e3-4131-918d-4d0466f6d843)

