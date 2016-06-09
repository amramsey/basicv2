package sixtyfour.elements;

public class ProgramCounter
{

  private int lineCnt;
  private int linePos;


  public ProgramCounter(int cnt, int pos)
  {
    this.lineCnt = cnt;
    this.linePos = pos;
  }


  public int getLinePos()
  {
    return linePos;
  }


  public void setLinePos(int linePos)
  {
    this.linePos = linePos;
  }


  public int getLineCnt()
  {
    return lineCnt;
  }


  public void setLineCnt(int lineCnt)
  {
    this.lineCnt = lineCnt;
  }

}