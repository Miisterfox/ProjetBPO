package op�rations;

import film.Film;

import java.util.Arrays;

public interface Montage {
	char ENCADREMENT = '*';

	/**
	 * M�thode permettant d'encadrer un film.
	 *
	 * @param f Le film � encadrer
	 * @return Le film encadr� par 4 lignes d'�toiles ('*').
	 */
	static Film encadrer(Film f) {
		return new Film() {

			@Override
			public int hauteur() {
				return f.hauteur() + 2;
			}

			@Override
			public int largeur() {
				return f.largeur() + 2;
			}

			@Override
			public boolean suivante(char[][] �cran) {
				char[][] �cranOrigine = new char[f.hauteur()][f.largeur()];
				boolean res = f.suivante(�cranOrigine);

				for (int i = 0; i < �cran.length; i++) {
					if (i == 0 || i == �cran.length - 1) {
						Arrays.fill(�cran[i], ENCADREMENT);
					} else {
						�cran[i][0] = ENCADREMENT;
						�cran[i][�cran[i].length - 1] = ENCADREMENT;
					}
				}

				for (int i = 1; i <= �cranOrigine.length; i++) {
					System.arraycopy(�cranOrigine[i - 1], 0, �cran[i],
					                 1, �cranOrigine[i - 1].length);
				}

				return res;
			}

			@Override
			public void rembobiner() {
				f.rembobiner();
			}
		};
	}

	/**
	 * M�thode permettant de coller un film � la fin d'un autre.
	 *
	 * � noter : Si les deux films n'ont pas la m�me taille, le film r�sultant
	 * du collage doit �tre assez grand pour pouvoir contenir chacun des films.
	 *
	 * @param f1 Le film auquel on doit coller le second
	 * @param f2 Le film que l'on doit coller � la fin du premier
	 * @return La s�quence des deux films.
	 */
	static Film coller(Film f1, Film f2) {
		return new Film() {
			boolean premierFini = false;

			@Override
			public int hauteur() {
				return Math.max(f1.hauteur(), f2.hauteur());
			}

			@Override
			public int largeur() {
				return Math.max(f1.largeur(), f2.largeur());
			}

			@Override
			public boolean suivante(char[][] �cran) {
				boolean res = false;

				if (!premierFini) {
					res = f1.suivante(�cran);
					premierFini = !res;
					if (premierFini) {
						f1.rembobiner();
					}
				}
				if (premierFini) {
					res = f2.suivante(�cran);
				}

				return res;
			}

			@Override
			public void rembobiner() {
				if (!premierFini)
					f1.rembobiner();
				f2.rembobiner();
				premierFini = false;
			}
		};
	}

	/**
	 * M�thode permettant l'extraction d'un film � partir d'une image de d�but
	 * jusqu'� une image de fin.
	 *
	 * � noter : si le num�ro de la derni�re image d'un extrait de film est
	 * inf�rieur au num�ro de la premi�re image, l'extrait doit �tre un film
	 * vide.
	 *
	 * @param f     Le film dont on souhaite r�aliser un extrait
	 * @param d�but Le num�ro de la premi�re image de l'extrait
	 * @param fin   Le num�ro de la derni�re image de l'extrait
	 * @return L'extrait d�sign� par les num�ros de la premi�re et la derni�re
	 * image de l'extrait.
	 */
	static Film extraire(Film f, int d�but, int fin) {
		return new Film() {
			int cpt = 0;

			@Override
			public int hauteur() {
				return f.hauteur();
			}

			@Override
			public int largeur() {
				return f.largeur();
			}

			@Override
			public boolean suivante(char[][] �cran) {
				if (fin < d�but) return false;

				while (cpt < d�but) {
					f.suivante(�cran);
					cpt++;
				}

				if (cpt++ <= fin) {
					return f.suivante(�cran);
				}
				return false;
			}

			@Override
			public void rembobiner() {
				f.rembobiner();
				cpt = 0;
			}
		};
	}

	/**
	 * M�thode permettant d'incruster un film dans un film. Le point
	 * d'incrustation est d�sign� par les num�ros de ligne et de colonne que
	 * doit prendre le coin sup�rieur gauche du film devant �tre incrust� dans
	 * les images du film o� il est incrust�.
	 *
	 * � noter : Si les images incrust�es d�passent les images dans lesquelles
	 * elles sont incrust�es, les images incrust�es doivent automatiquement �tre
	 * tronqu�es de mani�re � tenir sur l'�cran du film o� elles sont incrust�es
	 *
	 * @param f1         Le film dans lequel on incruste le second
	 * @param f2         Le film � incruster dans le premier
	 * @param ligne      Le num�ro de ligne o� incruster le film
	 * @param colonne    Le num�ro de colonne o� incruster le film
	 * @return Un film incrust� dans un autre film.
	 */
	static Film incruster(Film f1, Film f2, int ligne, int colonne) {
		if (ligne < 0)
			ligne = 0;
		if (colonne < 0)
			colonne = 0;

		final int finalLigne = ligne;
		final int finalColonne = colonne;

		return new Film() {

			@Override
			public int hauteur() {
				return f1.hauteur();
			}

			@Override
			public int largeur() {
				return f1.largeur();
			}

			@Override
			public boolean suivante(char[][] �cran) {
				char[][] �cranIncrust� = new char[f2.hauteur()][f2.largeur()];

				boolean res = f1.suivante(�cran);

				if (f2.suivante(�cranIncrust�)) {
					for (int i = 0; i < �cranIncrust�.length; i++) {
						for (int j = 0; j < �cranIncrust�[i].length; j++) {
							if ((i + finalLigne) >= 0 &&
							    (i + finalLigne) < �cran.length &&
							    (j + finalColonne) >= 0 &&
							    (j + finalColonne) < (�cran[i + finalLigne].length))

								�cran[i + finalLigne][j + finalColonne] =
										�cranIncrust�[i][j];
						}
					}
				}

				return res;
			}

			@Override
			public void rembobiner() {
				f1.rembobiner();
				f2.rembobiner();
			}
		};
	}

	/**
	 * R�p�te un film un nombre donn� de fois.
	 *
	 * � noter : r�p�ter un film un nombre de fois inf�rieur � 1 doit retourner
	 * un film vide.
	 *
	 * @param f             Le film � r�p�ter
	 * @param nbR�p�titions Le nombre de fois que le film doit �tre r�p�t�
	 * @return Le film une fois r�p�t� le nombre de fois demand�.
	 */
	static Film r�p�ter(Film f, int nbR�p�titions) {
		return new Film() {
			int cpt = 0;

			@Override
			public int hauteur() {
				return f.hauteur();
			}

			@Override
			public int largeur() {
				return f.largeur();
			}

			@Override
			public boolean suivante(char[][] �cran) {
				if (cpt < nbR�p�titions) {
					boolean res = f.suivante(�cran);
					if (!res) {
						cpt++;
						f.rembobiner();
						if (cpt < nbR�p�titions) res = f.suivante(�cran);
					}
					return res;
				}
				return false;
			}

			@Override
			public void rembobiner() {
				f.rembobiner();
				cpt = 0;
			}
		};
	}
}