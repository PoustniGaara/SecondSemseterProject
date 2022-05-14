package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import model.Customer;
import model.Meal;
import model.Menu;
import model.Reservation;
import model.Table;

public class ReservationConcreteDAO implements ReservationDAO {

	private static ReservationConcreteDAO instance = new ReservationConcreteDAO();

	private ReservationConcreteDAO() {
	}

	public static ReservationConcreteDAO getInstance() {
		if (instance == null) {
			instance = new ReservationConcreteDAO();
		}
		return instance;
	}

	@Override
	public ArrayList<Reservation> read() {
		Connection con = DBConnection.getInstance().getDBcon();
		ArrayList<Reservation> reservations = new ArrayList<Reservation>();

		try {
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM Reservations");
			while (rs.next()) {
				int id = rs.getInt("reservationID");
				Timestamp timestamp = rs.getTimestamp("timestamp");
				int duration = rs.getInt("duration");
				int guests = rs.getInt("noOfGuests");
				String note = rs.getString("note");
				String phone = rs.getString("phone");
				Calendar cal = new GregorianCalendar();
				cal.setTimeInMillis(timestamp.getTime());

				Reservation reservation = new Reservation(cal, getTables(id));
				reservation.setDuration(duration);
				reservation.setGuests(guests);
				reservation.setId(id);
				reservation.setNote(note);
				reservation.setCustomer(CustomerConcreteDAO.getInstance().read(phone));
				reservation.setMenus(getMenus(id));

				reservations.add(reservation);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnection();
		}
		return reservations;
	}

	private ArrayList<Table> getTables(int id) {
		Connection con = DBConnection.getInstance().getDBcon();
		ArrayList<Table> tables = new ArrayList<Table>();

		try {
			Statement tablesStatement = con.createStatement();
			ResultSet tablesResultSet = tablesStatement
					.executeQuery("SELECT * FROM [Tables], ReservedTables, Reservations "
							+ "WHERE [Tables].layoutItemID = ReservedTables.layoutItemId AND ReservedTables.reservationID = "
							+ id);
			while (tablesResultSet.next()) {
				String name = tablesResultSet.getString("name");
				String type = tablesResultSet.getString("type");
				int capacity = tablesResultSet.getInt("capacity");

				Table table = new Table(name, type, capacity);
				tables.add(table);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnection();
		}
		return tables;
	}

	private ArrayList<Menu> getMenus(int id) {
		Connection con = DBConnection.getInstance().getDBcon();
		ArrayList<Menu> menus = new ArrayList<Menu>();

		try {
			Statement menusStatement = con.createStatement();
			ResultSet menusResultSet = menusStatement
					.executeQuery("SELECT Menus.* FROM Menus, ReservedMenus WHERE ReservedMenus.reservationID = " + id
							+ " AND ReservedMenus.menuID = Menus.menuID");
			while (menusResultSet.next()) {
				String name = menusResultSet.getString("name");
				int menuid = menusResultSet.getInt("menuID");
				Menu menu = new Menu(name, getMeals(menuid));
				menus.add(menu);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnection();
		}
		return menus;
	}

	private ArrayList<Meal> getMeals(int id) {
		Connection con = DBConnection.getInstance().getDBcon();
		ArrayList<Meal> meals = new ArrayList<Meal>();

		try {
			Statement menusStatement = con.createStatement();
			ResultSet menusResultSet = menusStatement.executeQuery("SELECT * FROM Meals WHERE menuID = " + id);
			while (menusResultSet.next()) {
				String name = menusResultSet.getString("name");
				String description = menusResultSet.getString("description");
				float price = menusResultSet.getFloat("price");
				Meal meal = new Meal(name, description, price);
				meals.add(meal);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnection();
		}
		return meals;
	}

	@Override
	public Reservation read(int id) {
		Connection con = DBConnection.getInstance().getDBcon();

		try {
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM Reservations WHERE reservationID = " + id);
			while (rs.next()) {
				Timestamp timestamp = rs.getTimestamp("timestamp");
				int duration = rs.getInt("duration");
				int guests = rs.getInt("noOfGuests");
				String note = rs.getString("note");
				String phone = rs.getString("phone");
				Calendar cal = new GregorianCalendar();
				cal.setTimeInMillis(timestamp.getTime());

				Reservation reservation = new Reservation(cal, getTables(id));
				reservation.setDuration(duration);
				reservation.setGuests(guests);
				reservation.setId(id);
				reservation.setNote(note);
				reservation.setCustomer(CustomerConcreteDAO.getInstance().read(phone));
				reservation.setMenus(getMenus(id));

				return reservation;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnection();
		}
		return null;
	}

	@Override
	public void create(Reservation reservation) throws SQLException {
		Connection con = DBConnection.getInstance().getDBcon();
		try {
			PreparedStatement ps = con
					.prepareStatement("INSERT INTO dbo.Reservations (timestamp, duration, noOfGuests, note, phone)"
							+ "VALUES (?,?,?,?,?)");
			Timestamp timestamp = new Timestamp(reservation.getTimestamp().getTimeInMillis());
			ps.setTimestamp(1, timestamp);
			ps.setInt(2, reservation.getDuration());
			ps.setInt(3, reservation.getGuests());
			ps.setString(4, reservation.getNote());
			ps.setString(5, reservation.getCustomer().getPhone());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
	}

	@Override
	public void delete(Reservation reservation) throws SQLException {
		Connection con = DBConnection.getInstance().getDBcon();
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM dbo.Reservations WHERE id=?");
			ps.setInt(1, reservation.getId());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
	}

	@Override
	public void update(Reservation reservation) throws SQLException {
		Connection con = DBConnection.getInstance().getDBcon();
		try {
			PreparedStatement ps = con.prepareStatement(
					"UPDATE dbo.Reservations SET timestamp=?, SET duration=?, SET noOfGuests=?, SET note=?, SET phone=?"
							+ "WHERE id=?");
			Timestamp timestamp = new Timestamp(reservation.getTimestamp().getTimeInMillis());
			ps.setTimestamp(1, timestamp);
			ps.setInt(2, reservation.getDuration());
			ps.setInt(3, reservation.getGuests());
			ps.setString(4, reservation.getNote());
			ps.setString(5, reservation.getCustomer().getPhone());
			ps.setInt(6, reservation.getId());
			ps.execute();

			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
	}

}
