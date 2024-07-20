package shared;
   
public class Ratings {
       
        private float cleaning;
        private float position;
        private float services;
        private float quality;

        public Ratings(float cleaning, float position, float services, float quality) {
            this.cleaning = cleaning;
            this.position = position;
            this.services = services;
            this.quality = quality;   
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
                  
                    "cleaning=" + cleaning +
                    ", position=" + position +
                    ", services=" + services +
                    ", quality=" + quality +
                    '}';
        }
    }
