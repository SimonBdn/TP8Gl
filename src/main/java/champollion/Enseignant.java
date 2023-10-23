package champollion;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
public class Enseignant extends Personne {

    // TODO : rajouter les autres méthodes présentes dans le diagramme UML

    private final Set<Intervention> planification = new HashSet<>();
    private final Map<UE, ServicePrevu> enseignements = new HashMap<>();

    public Enseignant(String nom, String email) {
        super(nom, email);
    }
public void ajoutIntervention(Intervention interv) {
        if (!enseignements.containsKey(interv.getMatiere())) {
            throw new IllegalArgumentException("La matière ne fait pas partie du programme");
        }
        planification.add(interv);
    }


public int heuresPlanifiee() {
        float result = 0f;
        for (Intervention inter : planification) {
            result += equivalentTD(inter.getType(), inter.getDuree());
        }
        return Math.round(result);
    }
    /**
     * Calcule le nombre total d'heures prévues pour cet enseignant en "heures équivalent TD" Pour le calcul : 1 heure
     * de cours magistral vaut 1,5 h "équivalent TD" 1 heure de TD vaut 1h "équivalent TD" 1 heure de TP vaut 0,75h
     * "équivalent TD" */

     public float equivalentTD(TypeIntervention type, int volumeHeure){
         float rep = 0f;
         switch (type) {
             case CM: rep = volumeHeure * 1.5f; break;
             case TD: rep = volumeHeure; break;
             case TP: rep = volumeHeure * 0.75f; break;
         }
         return rep;
     }

     /* @return le nombre total d'heures "équivalent TD" prévues pour cet enseignant, arrondi à l'entier le plus proche
     *
     */
    public int heuresPrevues() {
        float rep = 0;
        for (UE ue : enseignements.keySet()) {
            rep += heuresPrevuesPourUE(ue);
        }
        return Math.round(rep);
    }

    public boolean enSousService(){
        return heuresPrevues() < 192;
    }

    /**
     * Calcule le nombre total d'heures prévues pour cet enseignant dans l'UE spécifiée en "heures équivalent TD" Pour
     * le calcul : 1 heure de cours magistral vaut 1,5 h "équivalent TD" 1 heure de TD vaut 1h "équivalent TD" 1 heure
     * de TP vaut 0,75h "équivalent TD"
     *
     * @param ue l'UE concernée
     * @return le nombre total d'heures "équivalent TD" prévues pour cet enseignant, arrondi à l'entier le plus proche
     *
     */
    public int heuresPrevuesPourUE(UE ue) {
        float rep = 0f;
        ServicePrevu p = enseignements.get(ue);
        if (p != null) {
            rep = (float) (equivalentTD(TypeIntervention.CM, p.getVolumeCM())
                                + equivalentTD(TypeIntervention.TD, p.getVolumeTD())
                                + equivalentTD(TypeIntervention.TP, p.getVolumeTP()));
        }
        return Math.round(rep);
    }



    public void ajouteEnseignement(UE ue, int volumeCm, int volumeTd, int volumeTp){
        if(volumeCm < 0 || volumeTd < 0 || volumeTp < 0){
            throw new IllegalArgumentException("Les valeurs ne sont pas positives.");
        }
        ServicePrevu s = enseignements.get(ue);
        if(s == null){
            s = new ServicePrevu(volumeCm, volumeTd, volumeTp, ue);
            enseignements.put(ue, s);
        }
        else{
            s.setVolumeCM(volumeCm + s.getVolumeCM());
            s.setVolumeTD(volumeTd + s.getVolumeTD());
            s.setVolumeTP(volumeTp + s.getVolumeTP());
        }
    }
    /**
     * Ajoute un enseignement au service prévu pour cet enseignant
     *
     * @param ue l'UE concernée
     * @param volumeCM le volume d'heures de cours magitral
     * @param volumeTD le volume d'heures de TD
     * @param volumeTP le volume d'heures de TP
     */

    public Set<UE> UEPrevues() {
        return enseignements.keySet();
    }

    public int resteAPlanifier(UE ue, TypeIntervention type) {
        float planifiees = 0;
        ServicePrevu p = enseignements.get(ue);
        if (null == p)
            return 0;
        float aPlanifier = p.getVolumePour(type);
        for (Intervention interv : planification) {
            if ((ue.equals(interv.getMatiere())) && (type.equals(interv.getType()))) {
                planifiees += interv.getDuree();
            }
        }
        return Math.round(aPlanifier - planifiees);
    }

}
