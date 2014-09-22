/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.api.math;

/**
 *
 * @author Stan
 */
public class Ray {
	/**
	 * Constructs a ray going from the first to the second point.
	 * 
	 * The norm of the ray will be the distance between the two points, this is,
	 * getX(1), getY(1) and getZ(1) will return the position of the second point.
	 * 
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
	 * @return 
	 */
	public static Ray fromPoints(
			float x1, float y1, float z1,
			float x2, float y2, float z2) {
		return new Ray(
				x1,
				y1,
				z1,
				x2 - x1,
				y2 - y1,
				z2 - z1);
	}
	
	/**
	 * Constructs a normalized ray from the first to the second point.
	 * 
	 * The norm of the ray will be 1, that is, getX(1), getY(1) and getZ(1) will
	 * return a point that is at distance 1 from the starting point, no matter
	 * the distance between the given points.
	 * 
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
	 * @return 
	 */
	public static Ray fromPointsNormalized(
			double x1, double y1, double z1,
			double x2, double y2, double z2) {
		double dirx = x2 - x1;
		double diry = y2 - y1;
		double dirz = z2 - z1;
		
		double length = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
		
		return new Ray(
				x1,
				y1,
				z1,
				dirx / length,
				diry / length,
				dirz / length);
	}
	
	/**
	 * Constructs a ray starting from the given point and going in the specified
	 * direction.
	 * 
	 * The norm of the ray will be equal to the length of dir, that is, getX(1),
	 * getY(1) and getZ(1) will return the point of start + dir no matter the
	 * length of dir.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param dirx
	 * @param diry
	 * @param dirz
	 * @return 
	 */
	public static Ray fromDirection(
			double x, double y, double z,
			double dirx, double diry, double dirz) {
		return new Ray(x, y, z, dirx, diry, dirz);
	}
	
	/**
	 * Constructs a normalized ray starting from the given point and going in
	 * the specified direction.
	 * 
	 * The norm of the ray will be equal to 1 no matter the length of dir.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param dirx
	 * @param diry
	 * @param dirz
	 * @return 
	 */
	public static Ray fromDirectionNormalized(
			double x, double y, double z,
			double dirx, double diry, double dirz) {
		return new Ray(x, y, z, dirx, diry, dirz);
	}
	
	private final double startx;
	private final double starty;
	private final double startz;
	private final double dirx;
	private final double diry;
	private final double dirz;
	
	private Ray(double x, double y, double z, double dirx, double diry, double dirz) {
		this.startx = x;
		this.starty = y;
		this.startz = z;
		
		this.dirx = dirx;
		this.diry = diry;
		this.dirz = dirz;
	}
	
	public double getStartX() {
		return startx;
	}
	
	public double getStartY() {
		return starty;
	}
	
	public double getStartZ() {
		return startz;
	}
	
	/**
	 * Gets the x position at the given distance. Distance is a multiple of the
	 * norm of this ray.
	 * 
	 * @param distance distance
	 * @return x value at the given distance
	 */
	public double getX(double distance) {
		return startx + dirx * distance;
	}
	
	/**
	 * Gets the y position at the given distance. Distance is a multiple of the
	 * norm of this ray.
	 * 
	 * @param distance distance
	 * @return y value at the given distance
	 */
	public double getY(double distance) {
		return starty + diry * distance;
	}
	
	/**
	 * Gets the z position at the given distance. Distance is a multiple of the
	 * norm of this ray.
	 * 
	 * @param distance distance
	 * @return z value at the given distance
	 */
	public double getZ(double distance) {
		return startz + dirz * distance;
	}
	
	/**
	 * Calculates the intersection with the given YZ plane (the collection of
	 * all points with given x - value). Returns the distance from the start.
	 * use getX, getY and getZ to calculate the actual coordinates. May return
	 * NaN if the ray is parallel to the plane.
	 * 
	 * @param x plane x value
	 * @return intersection distance
	 */
	public double intersectYZPlane(double x) {
		// startx + dirx * distance = x
		// distance = (x - startx) / dirx;
		return (x - this.startx) / dirx;
	}
	
	/**
	 * Calculates the intersection with the given XZ plane (the collection of
	 * all points with given y - value). Returns the distance from the start.
	 * use getX, getY and getZ to calculate the actual coordinates. May return
	 * NaN if the ray is parallel to the plane.
	 * 
	 * @param y plane y value
	 * @return intersection distance
	 */
	public double intersectXZPlane(double y) {
		// starty + diry * distance = y
		return (y - this.starty) / diry;
	}
	
	/**
	 * Calculates the intersection with the given XY plane (the collection of
	 * all points with given z - value). Returns the distance from the start.
	 * use getX, getY and getZ to calculate the actual coordinates. May return
	 * NaN if the ray is parallel to the plane.
	 * 
	 * @param z plane z value
	 * @return intersection distance
	 */
	public double intersectXYPlane(double z) {
		// startz + dirz * distance = z
		return (z - this.startz) / dirz;
	}
	
	/**
	 * Calculates the intersection of this ray with the given plane with
	 * formula ax + by + cz + d = 0.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @return 
	 */
	public double intersectPlane(double a, double b, double c, double d) {
		// ax + by + cz + d = 0
		// x = startx + distance * dirx
		// y = starty + distance * diry
		// z = startz + distance * dirz
		// distance * (dirx * a + diry * b + dirz * c) + (startx * a + starty * b + startz * c) + d = 0
		return - (startx * a + starty * b + startz * c + d) / (dirx * a + diry * b + dirz * c);
	}
	
	/**
	 * Gets the distance between point on the axis that's nearest to the given
	 * point, and the start of the ray.
	 * 
	 * @param px
	 * @param py
	 * @param pz
	 * @return distance
	 */
	public double nearestDistance(double px, double py, double pz) {
		// This is equal to the intersection between the plane perpendicular to
		//   the ray and going through (px, py, pz), and the ray.
		// 
		// So we can calculate as follows:
		//   1) Calculate the planar equation a * x + b * y + c * z = d
		//   2) The ray's calculation is
		//        x = startx + distance * dirx,
		//        y = starty + distance * diry,
		//        z = startz + distance * dirz)
		//   3) Fill in the ray's parametrized x, y, z position in the planar
		//        equation and calculate distance out of it
		// 
		// Planar equation perpendicular to the direction (dirx, diry, dirz) is
		//     x * dirx + y * diry + z * dirz = d
		// 
		// Thus we already have a = dirx, b = diry, c = dirz and d still needs
		//   to be calculated. We can achieve this by filling in the point in 
		//   the plane:
		//     px * dirx + py * diry + pz * dirz = d
		// 
		// Filling the parametric ray equation:
		//     (startx + distance * dirx) * dirx
		//   + (starty + distance * diry) * diry
		//   + (startz + distance * dirz) * dirz
		//   = px * dirx + py * diry + pz * dirz
		//
		// Working out the brackets:
		//      startx * dirx + distance * dirx * dirx
		//    + starty * diry + distance * diry * diry
		//    + startz * dirz + distance * dirz * dirz
		//    = px * dirx + py * diry + pz * dirz
		// 
		// Then factoring out distance:
		//     distance * (dirx * dirx + diry * diry + dirz * dirz)
		//   = px * dirx - startx * dirx
		//   + py * diry - starty * diry
		//   + pz * dirz - startz * dirz
		//
		// We can simplify that a bit:
		//
		//    distance * (dirx * dirx + diry * diry + dirz * dirz)
		//    = (px - startx) * dirx + (py - starty) * diry + (pz - startz) * dirz
		//
		// Thus calculating distance as such:
		
		return dirx * (px - startx) + diry * (py - starty) + dirz * (pz - startz)
				/ (dirx * dirx + diry * diry + dirz * dirz);
	}
}
