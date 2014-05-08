package com.bartoleo.gflappyasciibird.entity

public class Pipe {

    public double x
    public int height
    public int hole
    public boolean scored = false
    public String pipeStart = """\
[=======]"""
    public String pipeMiddle = """\
 |    #|"""
    int width = pipeStart.length()

}
