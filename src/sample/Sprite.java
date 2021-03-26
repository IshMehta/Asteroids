package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Sprite {
    public Vector position;
    public Vector velocity;
    public double rotation;
    public Rectangle boundary;
    public Image image;
    public double elapsedTime; //seconds

    public Sprite () {
        this.position = new Vector();
        this.velocity = new Vector();
        this.rotation = 0;
        this.boundary = new Rectangle();
        this.elapsedTime = 0;
    }

    public Sprite (String filename) {
        this();
        setImage(filename);
    }

    public void setImage(String filename) {
        this.image =  new Image (filename);
        this.boundary.setSize(this.image.getWidth(), this.image.getHeight());
    }

    public Rectangle getBoundary() {
        this.boundary.setPosition(this.position.x, this.position.y);
        return this.boundary;
    }

    public boolean overlaps (Sprite other) {
        return this.getBoundary().overlaps(other.getBoundary());
    }

    public void update (double deltaTime) {
        //increase elapsed time for this sprite
        this.elapsedTime+=deltaTime;

        //update posiition according to velocity
        this.position.add(this.velocity.x*deltaTime, this.velocity.y*deltaTime);
        this.wrap(800,600);
    }

    public void wrap(double screenWidth, double screenHeight) {
        double halfWidth = this.image.getWidth()/2;
        double halfHeight = this.image.getHeight()/2;
        if (this.position.x + halfWidth < 0) {
            this.position.x = screenWidth + halfWidth;
        }
        if (this.position.x > screenWidth + halfWidth) {
            this.position.x = -halfWidth;
        }
        if (this.position.y + halfHeight < 0) {
            this.position.y = screenHeight + halfHeight;
        }
        if (this.position.y > screenHeight + halfHeight) {
            this.position.y = -halfHeight;
        }
    }

    public void render(GraphicsContext context) {
        context.save();
        context.translate(this.position.x, this.position.y);
        context.rotate(this.rotation);
        // rotation takes place about the origin, to compensate for that we do this
        context.translate(-this.image.getWidth()/2, -this.image.getHeight()/2);
        context.drawImage(this.image, 0,0);

        // context opperations happen from last to frist
        context.restore();
    }
}
