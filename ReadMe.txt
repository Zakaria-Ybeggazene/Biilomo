Biilomo: projet de l'UE de Java Objet pour l'année 2020/2021, Département MIDO de l'Université Paris Dauphine-PSL.

Auteurs: 	Johana Labou
		Zakaria Ybeggazene


Introduction:

Ce projet a été implémenté avec IntelliJ. Certaines erreurs mineures d'affichage sont à prévoir s'il est ouvert avec un autre IDE.

Pour l'ouvrir, veuillez cloner l'URL suivant dans votre IDE: https://github.com/Zakaria-Ybeggazene/Biilomo.git

--------------------------------------------------------------------------------------------------------------------

Bienvenue dans la simulation de l'entrepôt Biilomo !


Tout d'abord, l'utilisateur est invité à initialiser les données de l'entrepôt. Il lui suffit de suivre les instructions affichées à l'écran.

Il est ensuite demandé à l'utilisateur selon quel mode il souhaite utiliser le programme. En effet, les différentes consignes reçues à chaque pas de temps pourront être données selon deux modes:

1) Par la console : un menu permettra alors à l’utilisateur d’indiquer un événement (recevoir un nouveau lot/ nouvelle commande de meuble/ rien) à chaque pas de temps.

2) Par un fichier text : chaque ligne du fichier text correspondra à un pas de temps et indiquera une consigne pour l’entrepôt (recevoir un nouveau lot/ nouvelle commande de meuble/ rien). Chaque ligne suivra l’une des formes suivantes
— <id> rien
— <id> lot <nom> <poids> <prix> <volume>
— <id> meuble <nom> <pieceMaison> <duréeConstruction> <typeLot1> <volumeLot1> <typeLot2> <volumeLot2> ...
Où id est un int qui représente l’identifiant de la consigne ; nom est un String qui identifie le nom de la pièce (porte, charnière, tiroir, poignée, cheville, vis, planche, tasseau, équerre, boulon...) ou du meuble (commode, lit, étagère, placard...) ; pieceMaison est un String qui donne la pièce de la maison associée au meuble (tout nom de type de pièce ou de meuble est autorisé) ; duréeConstruction est un int qui précise la durée de construction d’un meuble ; poids et prix sont des double et volume est un int.


L'utilisateur se voit donc effectuer le choix suivant: (1) mode console (2) mode fichier texte.

A chaque fois que ce genre de choix lui est proposé, ce dernier doit choisir en entrant l'entier correspondant au choix. Par exemple, s'il souhaite exécuter la simulation par le mode console, il devra saisir 1.

Si l'utilisateur choisit le mode console, il lui suffit de suivre les instructions affichées à l'écran.
S'il choisit le fichier texte, l'utilisateur doit bien s'assurer d'avoir un fichier au bon format. Il pourra constater les résultats sur l'écran également. 


