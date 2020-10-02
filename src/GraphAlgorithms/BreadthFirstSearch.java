package GraphAlgorithms;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BreadthFirstSearch {
    private final List<Point> path = new ArrayList<Point>();

    int dist[][] = new int[23][33];
    Point prev[][] = new Point[23][33];

    public BreadthFirstSearch(GraphVisualize g){
        g.whichAlgorithm = "bfs";
        g.status = BFS(g);

        if (g.status){
            if (g.willAnimate)
                g.sleep(250);
            Point crawl = new Point(g.targetX, g.targetY);
            path.add(crawl);

            while (prev[crawl.y][crawl.x] != null){
                path.add(crawl);
                crawl = prev[crawl.y][crawl.x];
            }

            for (int i = path.size() - 1; i >= 0 ; i--) {
                g.grid[path.get(i).y][path.get(i).x] = 3;
                g.Update();
                if (g.willAnimate)
                    g.sleep(20);
            }

        }

        g.Update();
    }

    public boolean BFS(GraphVisualize g){
        Queue<Point> q = new LinkedList<>();
        //g.grid[g.sourceY][g.sourceX] = 3;

        //Initializing path and distances of the grid points
        for (int i = 0; i<g.grid.length; i++){
            for (int j = 0; j < g.grid[0].length; j++) {
                dist[i][j] = 99999;
                //prev[i][j] = new Point(-1, -1);
                prev[i][j] = null;
            }
        }

        //Add the source to the queue
        q.add(new Point(g.sourceX, g.sourceY));
        dist[g.sourceY][g.sourceX] = 0;

        while (!q.isEmpty()){
            //Pop the first item of the queue since its neighbours are visited
            Point v = q.remove();
            g.place++;
            if (g.grid[v.y][v.x] == 0)
                g.level[v.y][v.x] = g.place;





            //Visiting all the neighbours of the current point

            //top
            if (g.grid[v.y-1][v.x] == 0){
                g.grid[v.y-1][v.x] = 5;
                dist[v.y-1][v.x] = dist[v.y][v.x] + 1;
                prev[v.y-1][v.x] = new Point(v.x, v.y);
                q.add(new Point(v.x, v.y-1));
                g.level[v.y-1][v.x] = g.level[v.y][v.x]+1;
            }

            //right
            if (g.grid[v.y][v.x+1] == 0){
                g.grid[v.y][v.x+1] = 5;
                dist[v.y][v.x+1] = dist[v.y][v.x] + 1;
                prev[v.y][v.x+1] = new Point(v.x, v.y);
                q.add(new Point(v.x+1, v.y));
                g.level[v.y][v.x+1] = g.level[v.y][v.x]+1;
            }

            //bottom
            if (g.grid[v.y+1][v.x] == 0){
                g.grid[v.y+1][v.x] = 5;
                dist[v.y+1][v.x] = dist[v.y][v.x] + 1;
                prev[v.y+1][v.x] = new Point(v.x, v.y);
                q.add(new Point(v.x, v.y+1));
                g.level[v.y+1][v.x] = g.level[v.y][v.x]+1;
            }

            //left
            if (g.grid[v.y][v.x-1] == 0){
                g.grid[v.y][v.x-1] = 5;
                dist[v.y][v.x-1] = dist[v.y][v.x] + 1;
                prev[v.y][v.x-1] = new Point(v.x, v.y);
                q.add(new Point(v.x-1, v.y));
                g.level[v.y][v.x-1] = g.level[v.y][v.x]+1;
            }

            //If reached the target
            if (v.x == g.targetX && v.y == g.targetY){
                return true;
            }else if (v.x+1 == g.targetX && v.y == g.targetY){
                dist[v.y][v.x+1] = dist[v.y][v.x] + 1;
                prev[v.y][v.x+1] = new Point(v.x, v.y);
                return true;
            }else if (v.x-1 == g.targetX && v.y == g.targetY){
                dist[v.y][v.x-1] = dist[v.y][v.x] + 1;
                prev[v.y][v.x-1] = new Point(v.x, v.y);
                return true;
            }else if (v.x == g.targetX && v.y-1 == g.targetY){
                dist[v.y-1][v.x] = dist[v.y][v.x] + 1;
                prev[v.y-1][v.x] = new Point(v.x, v.y);
                return true;
            }else if (v.x == g.targetX && v.y+1 == g.targetY){
                dist[v.y+1][v.x] = dist[v.y][v.x] + 1;
                prev[v.y+1][v.x] = new Point(v.x, v.y);
                return true;
            }

            g.Update();
            if (g.willAnimate)
                g.sleep(g.getAnimSpeed);

        }


        return false;
    }


}
