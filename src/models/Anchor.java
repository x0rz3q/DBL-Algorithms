//package models;
//
//public class Anchor implements AnchorInterface {
//    private Double x;
//    private Double y;
//
//    public Anchor(double x, double y) {
//        this.x = x;
//        this.y = y;
//    }
//
//    @Override
//    public Double getX() {
//        return this.x;
//    }
//
//    @Override
//    public Double getY() {
//        return this.y;
//    }
//
//    @Override
//    public Double distance(AnchorInterface anchor) {
//        return (Math.pow(this.getX() - anchor.getX(),2) + Math.pow(this.getY() - anchor.getY(), 2));
//    }
//
//    @Override
//    public Boolean equals(AnchorInterface anchor) {
//        return this.getX().equals(anchor.getX()) && this.getY().equals(anchor.getY());
//    }
//}
