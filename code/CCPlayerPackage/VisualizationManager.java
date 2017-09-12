package CCPlayerPackage;

import Visualization.Visualization;

public class VisualizationManager {
    private static VisualizationManager ourInstance = new VisualizationManager();

    public static VisualizationManager getInstance() {
        return ourInstance;
    }

    private static Visualization myVisualization = null;

    private VisualizationManager() {
    }

    public static Visualization getMyVisualization() {

        if (myVisualization == null)
            myVisualization = new Visualization("kolleje");

        return myVisualization;
    }

    public static void setMyVisualization(Visualization myVisualization) {
        VisualizationManager.myVisualization = myVisualization;
    }
}
