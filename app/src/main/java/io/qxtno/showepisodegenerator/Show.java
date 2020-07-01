package io.qxtno.showepisodegenerator;

class Show{
    private String title;
    private int[] seasons;

    Show(String title, int[] seasons) {
        this.title = title;
        this.seasons = seasons;
    }

    String getTitle() {
        return title;
    }

    int[] getSeasons() {
        return seasons;
    }
}
