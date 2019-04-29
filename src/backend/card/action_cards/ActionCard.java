package backend.card.action_cards;

import backend.card.AbstractCard;

import java.util.List;

public abstract class ActionCard extends AbstractCard {
        private String type;
        private String text;

        public ActionCard(String type, String text) {
                this.type = type;
                this.text = text;
        }

        public String getActionType() {
                return type;
        }

        public String getText(){
                return text;
        }

        public abstract List<Object> getParameters();

        public void setType(String s){
        type = s;
        }

        public void setText(String t) {text = t;}
}
