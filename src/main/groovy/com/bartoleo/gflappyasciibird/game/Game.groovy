package com.bartoleo.gflappyasciibird.game

import com.bartoleo.gflappyasciibird.entity.Pipe
import com.bartoleo.gflappyasciibird.entity.Player
import com.bartoleo.gflappyasciibird.graphic.Renderer
import com.bartoleo.gflappyasciibird.graphic.RenderConfig
import com.bartoleo.gflappyasciibird.input.CharacterInputListener
import com.bartoleo.gflappyasciibird.util.MathUtils
import squidpony.squidgrid.gui.awt.event.SGMouseListener
import squidpony.squidgrid.gui.swing.SwingPane

import javax.swing.*
import javax.swing.event.MouseInputListener
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class Game {

    public static int DELAY = 1000 / 60
    public static double GRAVITY = 0.035
    public static double JUMP = -0.8
    public static double SPEED = 0.25

    public GameState state = GameState.game

    public SwingPane display
    public JFrame frame
    public Renderer renderer
    public Player player

    public int score = 0
    public ArrayList<Pipe> pipeList

    public Game() {

        // Setup window

        // Generate map

        renderer = new Renderer(RenderConfig.screenWidth, RenderConfig.screenHeight)

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

        this.state = GameState.menu

        javax.swing.Timer timer = new javax.swing.Timer(DELAY, new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                stepSim();
            }
        });
        timer.setRepeats(true);
        timer.start();

    }


    public void render() {

        clear(display)

        renderer.render(display: display, game: this, player: player, pipeList: pipeList, score: score)

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

        if (this.state == GameState.game) {

            player.dy += GRAVITY
            player.y += player.dy

            if (player.y + player.height >= RenderConfig.gameWindowY) {
                this.state = GameState.gameOver
            }

            pipeList.each {
                it.x -= SPEED
                if (!it.scored && it.x < player.x) {
                    score++
                    it.scored = false
                }
                if (MathUtils.checkCollide(player.x, player.y as int, player.width, player.height, it.x as int, 0, it.width, it.height) ||
                        MathUtils.checkCollide(player.x, player.y as int, player.width, player.height, it.x as int, it.height + it.hole, it.width, RenderConfig.gameWindowY - it.height - it.hole)) {
                    this.state = GameState.gameOver
                }
            }

            pipeList.removeAll {
                it.x <= -it.width
            }


            if (pipeList.size() == 0 || (pipeList.size() == 1 && pipeList.get(0).x < RenderConfig.gameWindowX / 2)) {
                Pipe pipe
                pipe = new Pipe()
                pipe.x = RenderConfig.gameWindowX
                pipe.hole = MathUtils.getIntInRange(15, 30)
                pipe.height = MathUtils.getIntInRange(1, RenderConfig.gameWindowY - pipe.hole - 1)
                pipeList.add(pipe)
            }

        }

        render()

    }


    public void jump() {
        if (this.state == GameState.game) {

            player.dy = JUMP

        } else if (this.state == GameState.menu) {

            this.state = GameState.game

        }
    }

    public void escape() {
        if (this.state == GameState.game) {

            this.state = GameState.gameOver

        }
    }

}
