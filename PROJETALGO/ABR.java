/*
 * le code a été fait en binome

 *membre du groupe : DOUADI Anis
                     ABDELLI Said
 */

import java.util.*;

/* @param <E>
 * le type des clés stockées dans l'arbre
 */
public class ABR<E> extends AbstractCollection<E> {
    private Noeud racine;
    private int taille;
    private Comparator<? super E> cmp;

    private class Noeud {
        E cle;
        Noeud gauche;
        Noeud droit;
        Noeud pere;

        Noeud(E cle) {
            this.cle = cle;
            this.gauche = null;
            this.droit = null;
        }

        /**
         * Renvoie le noeud contenant la clé minimale du sous-arbre enraciné
         * dans ce noeud
         *
         * @return le noeud contenant la clé minimale du sous-arbre enraciné
         *         dans ce noeud
         */
        Noeud minimum() {
            Noeud x = this;

            if (this == null) return null;

            while (x.gauche != null) x = x.gauche;
            return x;
        }

        /**
         * Renvoie le successeur de ce noeud
         *
         * @return le noeud contenant la clé qui suit la clé de ce noeud dans
         *         l'ordre des clés, null si c'est le noeud contenant la plus
         *         grande clé
         */
        Noeud suivant() {

            Noeud x = this;
            Noeud y = null;

            if (this == null) return null;

            if (x.droit != null) return x.droit.minimum();
            y = x.pere;
            while (y != null && x.equals(y.droit)) {
                x = y;
                y = y.pere;
            }

            return y;
        }
    }

    // Consructeurs

    /**
     * Crée un arbre vide.
     */
    public ABR() {
        this.racine = null;
        this.taille = 0;
        this.cmp = (e1, e2) -> ((Comparable<E>) e1).compareTo(e2);
    }

    /**
     * Crée un arbre vide. Les éléments sont comparés selon l'ordre imposé par
     * le comparateur
     *
     * @param cmp : le comparateur utilisé pour définir l'ordre des éléments
     */
    public ABR(Comparator<? super E> cmp) {
        this.racine = null;
        this.taille = 0;
        this.cmp = cmp;
    }

    /**
     * Constructeur par recopie. Crée un arbre qui contient les mêmes éléments
     * que c. L'ordre des éléments est l'ordre naturel.
     *
     * @param c :la collection à copier
     */
    public ABR(Collection<? extends E> c) {

        this.racine = null;
        this.taille = 0;
        this.cmp = (e1, e2) -> ((Comparable<E>) e1).compareTo(e2);
        addAll(c);

    }


    @Override
    public Iterator<E> iterator() {
        return new ABRIterator();
    }

    @Override
    public int size() {
        return taille;
    }

    // Quelques méthodes utiles

    /**
     * Recherche une clé.
     *
     * @param o : la clé à chercher
     * @return le noeud qui contient la clé ou null si la clé n'est pas trouvée.
     */
    private Noeud rechercher(Object o) {
        Noeud x = racine;

        if (o == null) return null;

        while (x != null && x.cle != o) {

        }
        return x;

    }

    /**
     * Supprime le noeud z.
     *
     * @param z
     *            le noeud à supprimer
     * @return le noeud contenant la clé qui suit celle de z dans l'ordre des
     *         clés.
     */
    private Noeud supprimer(Noeud z) {
        if (z == null) return null;

        Noeud y;
        Noeud suiv = z.suivant();

        if (z.gauche == null || z.droit == null) y = z;
        else y = z.suivant();
        // y est le seul noeud à détacher

        Noeud x;

        if (y == null) return null;

        if (y.gauche != null) x = y.gauche;
        else x = y.droit;
        // x est le fils unique de y ou null si il n'y a pas de fils

        if (x != null) x.pere = y.pere;

        if (y.pere == null) // suppression de la racine
        {
            this.racine = x;
        } else {
            if (y.equals(y.pere.gauche)) y.pere.gauche = x;
            else y.pere.droit = x;
        }

        if (!y.equals(z))
            z.cle = y.cle;

        this.taille--;

        return suiv;
    }

    /**
     * Les itérateurs doivent parcourir les éléments dans l'ordre
     */
    private class ABRIterator implements Iterator<E> {
        private Noeud courant;
        private Noeud suivant;

        public ABRIterator() {
            super();

            this.courant = null;
            this.suivant = ABR.this.racine.minimum();
        }

        public boolean hasNext() {
            return ABR.this.taille > 0 && this.suivant != null;
        }

        public E next() {
            this.courant = this.suivant;
            this.suivant = this.suivant.suivant();

            return this.courant.cle;
        }

        public void remove() {
            ABR.this.supprimer(this.courant);
        }
    }


    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        toString(racine, buf, "", maxStrLen(racine));
        return buf.toString();
    }

    private void toString(Noeud x, StringBuffer buf, String path, int len) {
        if (x == null)
            return;
        toString(x.droit, buf, path + "D", len);
        for (int i = 0; i < path.length(); i++) {
            for (int j = 0; j < len + 6; j++)
                buf.append(' ');
            char c = ' ';
            if (i == path.length() - 1)
                c = '+';
            else if (path.charAt(i) != path.charAt(i + 1))
                c = '|';
            buf.append(c);
        }
        buf.append("-- " + x.cle.toString());
        if (x.gauche != null || x.droit != null) {
            buf.append(" --");
            for (int j = x.cle.toString().length(); j < len; j++)
                buf.append('-');
            buf.append('|');
        }
        buf.append("\n");
        toString(x.gauche, buf, path + "G", len);
    }

    private int maxStrLen(Noeud x) {
        return x == null ? 0 : Math.max(x.cle.toString().length(),
                Math.max(maxStrLen(x.gauche), maxStrLen(x.droit)));
    }

    @Override
    public boolean add(E e) {
        if (e == null) return false;

        Noeud z = new Noeud(e);
        Noeud y = null;
        Noeud x = racine;

        while (x != null) {
            y = x;

            if (cmp.compare(z.cle, x.cle) < 0)
                x = x.gauche;
            else
                x = x.droit;
        }

        z.pere = y;

        if (y == null) {
            racine = z;
        } else {
            if (this.cmp.compare(z.cle, y.cle) < 0) y.gauche = z;
            else y.droit = z;
        }

        z.gauche = z.droit = null;

        this.taille++;

        return true;
    }


    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c)
            if (!this.add(e))
                return false;

        return true;
    }

    public static void main(String[] args) {

        /*INSERTION CROISSANTE
          ABR<Integer> abr = new ABR<>();


        long start = System.currentTimeMillis();
        for(int i=0;i<150000;i++) {
        	abr.add(i);
        }
        long end = System.currentTimeMillis();
        System.out.println("Le temps d'éxécution est "+ (double)(end-start));*/

        /*INSERTION ALEATOIRE
         ABR<Integer> abrAl = new ABR<>();



        Random r = new Random();
        long start = System.currentTimeMillis();
        for(int i = 0; i < 150000; i++) {
            abrAl.add(r.nextInt(5));
        }
        long end = System.currentTimeMillis();
        System.out.println("Le temps d'éxécution est "+ (double)(end-start));*/



    	/*RECHERCHE AVEC L'INSERTION CROISSANTE
    	 * ABR<Integer> a = new ABR<>();

         System.out.println("\nAjouts de plusieurs valeurs");
         for(int i=0;i<10000;i++) {
         	a.add(i);
         }
         long start = System.currentTimeMillis();
         for(int j = 1; j<20000;j++) {
        	 System.out.println("\t-L'arbre contient le nombre"+j+ " Résultat: "  + a.contains(j));
         }

         long end = System.currentTimeMillis();
         System.out.println("Le temps d'éxécution est "+ (double)(end-start));*/


    	/*RECHERCHE AVEC L'INSERTION ALEATOIRE
         ABR<Integer> abrAl = new ABR<>();

         System.out.println("\n/------------------------/\n/    Arbre aléatoire     /\n/------------------------/");

         Random r = new Random();
         for(int i = 0; i < 10000; i++) {
             abrAl.add(r.nextInt(10));
         }
          long start = System.currentTimeMillis();
          for(int i = 1; i<20000;i++) {
         	 System.out.println("\t-L'arbre contient le nombre"+i+ " Résultat: "  + abrAl.contains(i));
          }

          long end = System.currentTimeMillis();
         System.out.println("Le temps d'éxécution est "+ (double)(end-start));
         */



    }
}
