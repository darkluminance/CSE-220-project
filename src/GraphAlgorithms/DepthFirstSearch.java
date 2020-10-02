package GraphAlgorithms;

import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class DepthFirstSearch {
    private final List<Integer> path = new ArrayList<Integer>();


    public DepthFirstSearch(GraphVisualize g, Point source){
        g.whichAlgorithm = "dfs";
        g.status = DFS(g, source.x, source.y, path);
        System.out.println(g.iterations);
        System.out.println(g.pathPlace);
        g.Update();
    }

    public boolean DFS(GraphVisualize g, int x, int y, List<Integer> path){
        if (g.willFind == false) {
            return false;
        }
        if(x<1 || x>= g.grid[0].length - 1 ||y<1 || y>= g.grid.length - 1 ){
            return false;
        }

        g.iterations++;
            //g.grid[y][x] = 69;



        //Reached the target
        if (g.grid[y][x] == 4){
            path.add(x);    path.add(y);
            g.iterations++;
            if (g.willAnimate)
                g.sleep(280);
            return true;
        }
        if (g.grid[y][y] != 1 && g.grid[y][y] != 4 && g.grid[y][y] != 2){
            g.current.x = x;
            g.current.y = y;
            g.Update();
            if (g.willAnimate)
                g.sleep(g.getAnimSpeed);
        }
        /*if (g.grid[y][x] == 2) {
            g.grid[y][x] = -1;
            g.Update();
            g.sleep(15);
        }

        if (g.grid[y][x] == -1){
            return false;
        }*/



        if (g.grid[y][x] == 0 || g.grid[y][x] == 2 || g.grid[y][x] == 69){
            if (g.grid[y][x] == 0)
            {
                g.grid[y][x] = 5;
                if (g.level[y][x]==0){
                    g.place++;
                    g.level[y][x] = g.place;
                }

            }else if(g.grid[y][x] == 69){
                g.grid[y][x] = 5;
            }


            g.Update();
            if (g.willAnimate)
                g.sleep(g.getAnimSpeed);


            if (DFS(g, x, y-1, path)){
                path.add(x);    path.add(y);
                g.grid[y][x] = 3;
                g.pathPlace++;
                g.pathPos.put(new Point(y, x), g.pathPlace);
                g.Update();
                if (g.willAnimate)
                    g.sleep(15);
                return true;
            }
            if (DFS(g, x+1, y, path)){
                path.add(x);    path.add(y);
                g.grid[y][x] = 3;
                g.pathPlace++;
                g.pathPos.put(new Point(y, x), g.pathPlace);
                g.Update();
                if (g.willAnimate)
                    g.sleep(15);
                return true;
            }
            if (DFS(g, x, y+1, path)){
                path.add(x);    path.add(y);
                g.grid[y][x] = 3;
                g.pathPlace++;
                g.pathPos.put(new Point(y, x), g.pathPlace);
                g.Update();
                if (g.willAnimate)
                    g.sleep(15);
                return true;
            }
            if (DFS(g, x-1, y, path)){
                path.add(x);    path.add(y);
                g.grid[y][x] = 3;
                g.pathPlace++;
                g.pathPos.put(new Point(y, x), g.pathPlace);
                g.Update();
                if (g.willAnimate)
                    g.sleep(15);
                return true;
            }

        }
        return false;
    }
}
