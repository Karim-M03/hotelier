package shared;

import java.time.LocalDateTime;    

public class Ratings {
       
        private int level;
        private float general;
        private float cleaning;
        private float position;
        private float services;
        private float quality;

        public Ratings(float general, float cleaning, float position, float services, float quality, int level) {
            this.level = level;
            this.general = general;
            this.cleaning = cleaning;
            this.position = position;
            this.services = services;
            this.quality = quality;
            
        }

        public float getGeneral() {
            return general;
        }

        public void setGeneral(float general) {
            this.general = general;
        }

        public float getCleaning() {
            return cleaning;
        }

        public void setCleaning(float cleaning) {
            this.cleaning = cleaning;
        }

        public float getPosition() {
            return position;
        }

        public void setPosition(float position) {
            this.position = position;
        }

        public float getServices() {
            return services;
        }

        public void setServices(float services) {
            this.services = services;
        }

        public float getQuality() {
            return quality;
        }

        public void setQuality(float quality) {
            this.quality = quality;
        }

        
        @Override
        public String toString() {
            return "Ratings{" +
                    "general=" + general +
                    "cleaning=" + cleaning +
                    ", position=" + position +
                    ", services=" + services +
                    ", quality=" + quality +
                    '}';
        }
    }
