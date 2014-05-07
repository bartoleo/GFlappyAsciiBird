package com.bartoleo.gflappyasciibird.map

import com.bartoleo.gflappyasciibird.entity.Pipe
import com.bartoleo.gflappyasciibird.entity.Player
import com.bartoleo.gflappyasciibird.graphic.RenderConfig
import squidpony.squidcolor.SColor
import squidpony.squidcolor.SColorFactory
import squidpony.squidgrid.gui.swing.SwingPane

class LevelMap {

    public int xSize
    public int ySize


    public LevelMap(int x, int y) {
        xSize = x
        ySize = y
    }

    /**
     * @param startx
     * @param starty
     */
    public void render(params) {

        SwingPane display = params.display
        Player player = params.player
        ArrayList<Pipe> pipeList = params.pipeList
        Integer score = params.score

        int xRange = RenderConfig.gameWindowX
        int yRange = RenderConfig.gameWindowY

        xRange.times { x ->
            display.placeCharacter(x, yRange, '-' as char, SColor.DARK_GRAY)
        }
        yRange.times { y ->
            display.placeCharacter(xRange, y, '|' as char, SColor.DARK_GRAY)
        }
        display.placeCharacter(xRange, yRange, '+' as char, SColor.DARK_GRAY)

        display.placeHorizontalString(xRange+2,2,"Score")
        display.placeHorizontalString(xRange+2,4,score.toString())

//
        int y = player.y
        int x = player.x
        player.character.eachLine {
            y++
            x = player.x
            for (char ch : it.toCharArray()) {
                x++
                display.placeCharacter(x, y, ch, SColorFactory.asSColor(255, 255, 255));
            }
        }
        pipeList.each {
            String pipeString
            for (int i=1;i<yRange;i++){
                if (i<it.height||i>it.height+it.hole){
                    pipeString = it.pipeMiddle
                } else if (i==it.height||i==it.height+it.hole){
                    pipeString = it.pipeStart
                } else {
                    pipeString=""
                }
                int px = it.x
                for (char ch : pipeString.toCharArray()) {
                    px++
                    if (px>0&&px<xRange){
                        display.placeCharacter(px, i, ch, SColorFactory.asSColor(255, 255, 255));
                    }
                }
            }
        }

    }

}
