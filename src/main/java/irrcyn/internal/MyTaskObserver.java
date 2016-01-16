package irrcyn.internal;

import irrcyn.View.MainFrame;
import irrcyn.internal.parser.ParserTask;
import org.cytoscape.task.read.LoadTableFileTaskFactory;
import org.cytoscape.work.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by seti on 11/01/16.
 */
public class MyTaskObserver implements TaskObserver {
    private String finishStatus;
    private List<Task> tasksFinished;
    private MainFrame mainFrame;
    private TaskManager tm;
    private LoadTableFileTaskFactory ldt;
    private String url;
    private String name;

    public MyTaskObserver(MainFrame mainFrame, TaskManager tm, LoadTableFileTaskFactory ldt, ParserTask parserTask){
        tasksFinished = new ArrayList<Task>();
        this.tm=tm;
        this.ldt=ldt;
        this.mainFrame = mainFrame;
        url = parserTask.getTargetCSV();
        name = parserTask.getName().split(".csv")[0];
    }

    @Override
    public void taskFinished(ObservableTask task) {
        tasksFinished.add(task);
    }

    @Override
    public void allFinished(FinishStatus finishStatus) {
        tm.execute(ldt.createTaskIterator(new File(url)));
        mainFrame.finishedLoading(name);
    }

    public List<Task> getTasksFinished() {
        return tasksFinished;
    }
}
