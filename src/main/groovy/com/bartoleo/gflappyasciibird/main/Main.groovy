package com.bartoleo.gflappyasciibird.main

import com.bartoleo.gflappyasciibird.entity.Pipe
import com.bartoleo.gflappyasciibird.entity.Player
import com.bartoleo.gflappyasciibird.game.Game
import com.bartoleo.gflappyasciibird.game.GameState
import com.bartoleo.gflappyasciibird.graphic.RenderConfig
import com.bartoleo.gflappyasciibird.input.CharacterInputListener
import com.bartoleo.gflappyasciibird.map.LevelMap
import com.bartoleo.gflappyasciibird.util.MathUtils
import squidpony.squidcolor.SColor
import squidpony.squidgrid.gui.awt.event.SGMouseListener
import squidpony.squidgrid.gui.swing.SwingPane

import javax.swing.*
import javax.swing.event.MouseInputListener
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

public class Main {

    public static int DELAY = 1000/60;

    public SwingPane display
    public JFrame frame
    public LevelMap levelMap
    public Player player

    public int selectX = 0
    public int selectY = 0
    public int score = 0
    public ArrayList<Pipe> pipeList


    public static void main(String[] args) {
        Main main = new Main()
    }

    public Main() {

        // Setup window

        // Generate map

        levelMap = new LevelMap(RenderConfig.screenWidth, RenderConfig.screenHeight)

        player = new Player()
        player.x = 10
        player.y = 10
        player.dy = 0

        pipeList = new ArrayList<>()

        // set up display
        frame = new JFrame("Groovy Flappy Ascii Bird")
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
        frame.setLayout(new BorderLayout())

        display = new SwingPane()
        display.initialize(RenderConfig.screenWidth, RenderConfig.screenHeight, new Font("Ariel", Font.BOLD, 12))
        clear(display)

        frame.add(display, BorderLayout.SOUTH)
        frame.setVisible(true)

        frame.pack()
        frame.setLocationRelativeTo(null)
        frame.repaint()

        frame.requestFocusInWindow()

        CharacterInputListener dil = new CharacterInputListener(this, player)

        int cellWidth = display.getCellDimension().width
        int cellHeight = display.getCellDimension().height
        MouseInputListener mil = new SGMouseListener(cellWidth, cellHeight, dil)
        display.addMouseListener(mil) //listens for clicks and releases
        display.addMouseMotionListener(mil) //listens for movement based events
        frame.addKeyListener(dil)

        render()

        Game.state = GameState.playing

        Timer timer = new Timer(DELAY,new ActionListener() {
            public void actionPerformed(ActionEvent event)
            {
                stepSim();
            }
        });
        timer.setRepeats(true);
        timer.start();

    }


    public void render() {

        clear(display)

        if (Game.state == GameState.selecting) {
            levelMap.render(display: display, player: player, pipeList:pipeList, score:score, viewX: selectX, viewY: selectY)
        } else {
            levelMap.render(display: display, player: player, pipeList:pipeList, score:score)
        }

        //render stats
        //MessageLog.render(display, player)

        //done rendering this frame
        display.refresh();
    }


    public void clear(SwingPane display) {
        for (int x = 0; x < RenderConfig.screenWidth; x++) {
            for (int y = 0; y < RenderConfig.screenHeight; y++) {
                display.clearCell(x, y)
            }
        }
        display.refresh()
    }

    public void stepSim() {

        if (Game.state == GameState.playing) {

            player.dy += 0.05
            player.y += player.dy

            if (player.y+3>RenderConfig.gameWindowY){
                Game.state = GameState.dead
            }

            pipeList.each {
                it.x -= 0.2
            }

            pipeList.removeAll {
                it.x<=7
            }


            if (pipeList.size()==0||(pipeList.size()==1&&pipeList.get(0).x<RenderConfig.gameWindowX/2)){
                Pipe pipe
                pipe = new Pipe()
                pipe.x = RenderConfig.gameWindowX
                pipe.hole = MathUtils.getIntInRange(15,30)
                pipe.height = MathUtils.getIntInRange(1, RenderConfig.gameWindowY-pipe.hole-1)
                pipeList.add(pipe)
            }

        }

        render()

    }


    public void jump() {
        if (Game.state == GameState.playing) {

            player.dy = -1

        }
    }

    public void escape() {
        if (Game.state == GameState.playing) {

            Game.state = GameState.dead

        }
    }


    private def oldState

    public void inspect() {

        if (Game.state != GameState.selecting) {
            oldState = Game.state
            Game.state = GameState.selecting
            selectX = player.x
            selectY = player.y
            display.setCellBackground(RenderConfig.windowRadiusX, RenderConfig.windowRadiusY, SColor.EDO_BROWN)
            render()

        } else {
            Game.state = oldState
            display.setCellBackground(RenderConfig.windowRadiusX, RenderConfig.windowRadiusY, SColor.BLACK)
            render()

        }
    }

    public void reload() {
        //doesnt do anything
    }
}