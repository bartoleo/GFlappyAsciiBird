package com.bartoleo.gflappyasciibird.input;

import com.bartoleo.gflappyasciibird.entity.Player;
import com.bartoleo.gflappyasciibird.game.Game;

import javax.swing.event.MouseInputListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

import static java.awt.event.KeyEvent.VK_ESCAPE;
import static java.awt.event.KeyEvent.VK_UNDEFINED;

/**
 * A simple input listener.
 */
public class CharacterInputListener implements MouseInputListener, KeyListener {


    Game context;
    Player player;

    public CharacterInputListener(Game context, Player player) {
        this.context = context;
        this.player = player;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        context.jump();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //nothing special happens
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //nothing special happens
    }

    @Override
    public void mouseDragged(MouseEvent e) {
//        dragged = true;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //nothing special happens
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //http://forums.codeguru.com/showthread.php?495419-KeyListener-doesn-t-work-on-Windows-OS
        handleKey(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private void handleKey(KeyEvent e) {

        int code = e.getExtendedKeyCode();

        // if ExtendedKeyCode is VK_UNDEFINED (0) use normal keycode
        if (code == VK_UNDEFINED) {
            code = e.getKeyCode();
        }

        boolean shift = e.isShiftDown();


        switch (code) {
            //movement
            case VK_ESCAPE:
                context.escape();
            default:
                context.jump();
                break;
        }
    }


}