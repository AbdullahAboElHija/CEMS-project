package control_lecturer;

import java.util.ArrayList;

import entity.Lecturer;
import entity.LoggedInUser;
import entity.Profession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class ProfessionsController {
	
	/**
	 * fills comboBox with professions of currently logged in lecturer
	 * @param profCombo professions combo fxml component
	 * @param professions list to fill in the professions
	 */
		public static void fillProfessionsCombo(ComboBox<Profession> profCombo, ObservableList<Profession> professions) {
			try {
				Lecturer lec= (Lecturer)LoggedInUser.getInstance().getUser();
				ArrayList<Profession> profs=new ArrayList<>(lec.getProfessions());
				professions=FXCollections.observableArrayList(profs);
				profCombo.setItems(professions);
				profCombo.setConverter(new StringConverter<Profession>() {
		            @Override
		            public String toString(Profession prof) {
		                return prof.getProfessionName();
		            }

		            @Override
		            public Profession fromString(String string) {
		                return null;
		            }
		        });
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
}
