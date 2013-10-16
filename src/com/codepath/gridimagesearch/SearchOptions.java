package com.codepath.gridimagesearch;

import java.io.Serializable;

public class SearchOptions implements Serializable {
	private static final long serialVersionUID = 3371672638472411175L;

	public enum Size {
		SMALL("small"), MEDIUM("medium"), LARGE("large"), EXTRALARGE("xlarge");
		public final String id;

		Size(String id) {
			this.id = id;
		}
		public String toString() {
			return id;
		}
	}

	public enum Color {
		BLACK("black"), BLUE("blue"), BROWN("brown"), GRAY("gray"), GREEN("green"), ORANGE("orange"),
		PINK("pink"), PURPLE("purple"), RED("red"), TEAL("teal"), WHITE("white"), YELLOW("yellow");
		public final String id;

		Color(String id) {
			this.id = id;
		}
		public String toString() {
			return id;
		}
	}

	public enum Type {
		FACE("face"), PHOTO("photo"), CLIPART("clipart"), LINEART("lineart");
		public final String id;

		Type(String id) {
			this.id = id;
		}
		public String toString() {
			return id;
		}
	}

	public String size = null;
	public String color = null;
	public String type = null;
	public String site = null;
}