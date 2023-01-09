import java.awt.image.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.io.*;
public class Main{
    public static void main(String[] args) {
        new Main();
    }

    Main(){
        Image image = getFromClipboard();
        sendToClipboard(invertImage(image));
    }

    Image getFromClipboard(){
        try{
            Thread.sleep(100);
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Clipboard clipboard = toolkit.getSystemClipboard();
            Image result = (Image) clipboard.getData(DataFlavor.imageFlavor);
            return result;
        }catch(Exception e){
            System.out.println("Couldn't read from clipboard");
            System.out.println(e);
            Thread.currentThread().interrupt();
        }
        return null;
    }

    public static BufferedImage invertImage(Image image) {
        BufferedImage bimage = toBufferedImage(image);

        for (int x = 0; x < bimage.getWidth(); x++) {
            for (int y = 0; y < bimage.getHeight(); y++) {
                int rgba = bimage.getRGB(x, y);
                Color col = new Color(rgba, true);
                col = new Color(255 - col.getRed(),
                                255 - col.getGreen(),
                                255 - col.getBlue());
                bimage.setRGB(x, y, col.getRGB());
            }
        }
        return bimage;
    }

    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    public static void sendToClipboard(BufferedImage bimage){
        new CopyImagetoClipBoard(bimage);
    }
}
//  ----------------------------------------------------------------------------------------------------------------------------------------------------------------
class CopyImagetoClipBoard implements ClipboardOwner {
    public CopyImagetoClipBoard(BufferedImage bimage) {
        
        BufferedImage i = bimage;
        TransferableImage trans = new TransferableImage( i );
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        c.setContents( trans, this );
    }

    public void lostOwnership( Clipboard clip, Transferable trans ) {
        System.out.println( "Lost Clipboard Ownership" );
    }

    private class TransferableImage implements Transferable {
        
        Image i;

        public TransferableImage( Image i ) {
            this.i = i;
        }

        public Object getTransferData( DataFlavor flavor )
        throws UnsupportedFlavorException, IOException {
            if ( flavor.equals( DataFlavor.imageFlavor ) && i != null ) {
                return i;
            }
            else {
                error();
                throw new UnsupportedFlavorException( flavor );
            }
        }

        public DataFlavor[] getTransferDataFlavors() {
            DataFlavor[] flavors = new DataFlavor[ 1 ];
            flavors[ 0 ] = DataFlavor.imageFlavor;
            return flavors;
        }

        public boolean isDataFlavorSupported( DataFlavor flavor ) {
            DataFlavor[] flavors = getTransferDataFlavors();
            for ( int i = 0; i < flavors.length; i++ ) {
                if ( flavor.equals( flavors[ i ] ) ) {
                    return true;
                }
            }

            return false;
        }
    }

    public static void error(){
        System.out.println("--------------");
        System.out.println("--------------");
    }
}