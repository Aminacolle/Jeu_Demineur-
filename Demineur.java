//importation des bibliotheques pour l'interface graphique
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Demineur extends JFrame {
    //initialiation des taille de grille et de bombe pour chaque niveau
    //debutant
    private static final int TAILLE_GRILLE = 9;
    private static final int NB_MINES = 10;
    //intermediare
    private static final int INT_TAILLE_GRILLE = 16;
    private static final int INT_NB_MINES = 40;
    //expert
    private static final int L_EX_TAILLE_GRILLE = 16;
    private static final int C_EX_TAILLE_GRILLE = 30;
    private static final int EX_NB_MINES = 99;

    private int[][] grilleMines = new int[TAILLE_GRILLE][TAILLE_GRILLE];
    private JButton[][] grilleJeu = new JButton[TAILLE_GRILLE][TAILLE_GRILLE];
    //importation des images
    private ImageIcon mineIcon = new ImageIcon("mine.png");
    private ImageIcon drapeauIcon = new ImageIcon("drapeau.png");
    private ImageIcon questionIcon = new ImageIcon("question.png");
    private int nbCasesVides;
//Constructeur avec la grille et les bombes
    public Demineur() {
        initGrilleMines();
        initGrilleJeu();
    }
    //initialise le nombre de bombe
    private void initGrilleMines() {
        for (int i = 0; i < NB_MINES; i++) {
            int x = (int) (Math.random() * TAILLE_GRILLE);
            int y = (int) (Math.random() * TAILLE_GRILLE);
            if (grilleMines[x][y] == -1) {
                i--;
            } else {
                grilleMines[x][y] = -1;
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        if (x+dx >= 0 && x+dx < TAILLE_GRILLE && y+dy >= 0 && y+dy < TAILLE_GRILLE && grilleMines[x+dx][y+dy] != -1) {
                            grilleMines[x+dx][y+dy]++;
                        }
                    }
                }
            }
        }
    }
    // grille de jeu
    private void initGrilleJeu() {
        JPanel grillePanel = new JPanel(new GridLayout(TAILLE_GRILLE, TAILLE_GRILLE));
        for (int i = 0; i < TAILLE_GRILLE; i++) {
            for (int j = 0; j < TAILLE_GRILLE; j++) {
                final int x = i;
                final int y = j;
                grilleJeu[i][j] = new JButton();
                grilleJeu[i][j].setFocusable(false);
                grilleJeu[i][j].addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            jouer(x,y);
                        } else if (e.getButton() == MouseEvent.BUTTON3) {
                            if (grilleJeu[x][y].getIcon() == null) {
                                grilleJeu[x][y].setIcon(drapeauIcon);
                            } else if (grilleJeu[x][y].getIcon() == drapeauIcon) {
                                grilleJeu[x][y].setIcon(questionIcon);
                            } else if (grilleJeu[x][y].getIcon() == questionIcon) {
                                grilleJeu[x][y].setIcon(null);
                            }
                        }
                    }
                });
                grillePanel.add(grilleJeu[i][j]);
            }
        }
        add(grillePanel);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void jouer(int i, int j) {
    if (grilleJeu[i][j].getIcon() != null) {
    // Si la case a déjà été marquée avec un drapeau ou un point d'interrogation, on ne fait rien
    return;
    }
    if (grilleMines[i][j] == -1) {
    // Si la case contient une mine, on a perdu
    grilleJeu[i][j].setIcon(mineIcon);
    JOptionPane.showMessageDialog(this, "BOOM! Vous avez perdu.", "Démineur", JOptionPane.INFORMATION_MESSAGE);
    reset();
    return;
    }
    // Sinon, on découvre les cases adjacentes
    decouvrirCasesAdjacentes(i, j);
    if (nbCasesVides == TAILLE_GRILLE * TAILLE_GRILLE - NB_MINES) {
    // Si toutes les cases vides ont été découvertes, on a gagné
    JOptionPane.showMessageDialog(this, "Félicitations, vous avez gagné!", "Démineur", JOptionPane.INFORMATION_MESSAGE);
    reset();
    }
    }
private void decouvrirCasesAdjacentes(int i, int j) {
    if (grilleMines[i][j] != 0) {
        // Si la case contient une mine ou est adjacente à une mine, on affiche le nombre de mines adjacentes
        grilleJeu[i][j].setText(Integer.toString(grilleMines[i][j]));
        grilleJeu[i][j].setEnabled(false);
        nbCasesVides--;
        return;
    }
    // Sinon, on continue de découvrir les cases adjacentes
    grilleJeu[i][j].setEnabled(false);
    nbCasesVides--;
    for (int x = i - 1; x <= i + 1; x++) {
        for (int y = j - 1; y <= j + 1; y++) {
            if (x >= 0 && x < TAILLE_GRILLE && y >= 0 && y < TAILLE_GRILLE && grilleJeu[x][y].isEnabled()) {
                decouvrirCasesAdjacentes(x, y);
            }
        }
    }
}

private void reset() {
    // Réinitialise la grille de jeu et la grille des mines
    for (int i = 0; i < TAILLE_GRILLE; i++) {
        for (int j = 0; j < TAILLE_GRILLE; j++) {
            grilleJeu[i][j].setEnabled(true);
            grilleJeu[i][j].setText("");
            grilleJeu[i][j].setIcon(null);
            grilleMines[i][j] = 0;
        }
    }
    initGrilleMines();
    nbCasesVides = TAILLE_GRILLE * TAILLE_GRILLE - NB_MINES;
}



public static void main(String[] args) {
    Demineur demineur = new Demineur();
}
}