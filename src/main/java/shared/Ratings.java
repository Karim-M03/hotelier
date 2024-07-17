package shared;

    public class Ratings {
        private int cleaning;
        private int position;
        private int services;
        private int quality;

        public Ratings(int cleaning, int position, int services, int quality) {
            this.cleaning = cleaning;
            this.position = position;
            this.services = services;
            this.quality = quality;
        }

        public int getCleaning() {
            return cleaning;
        }

        public void setCleaning(int cleaning) {
            this.cleaning = cleaning;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public int getServices() {
            return services;
        }

        public void setServices(int services) {
            this.services = services;
        }

        public int getQuality() {
            return quality;
        }

        public void setQuality(int quality) {
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
