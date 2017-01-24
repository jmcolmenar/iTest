package com.itest.entity;

import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the centros database table.
 * 
 */
@Entity
@Table(name="centros")
public class Centro  {

	@Id
	@Column(unique=true, nullable=false)
	private int idcentro;

	@Column(nullable=false, length=10)
	private String cod;

	@Column(length=5)
	private String cpostal;

	@Column(length=50)
	private String direccion;

	@Column(length=50)
	private String email;

	@Column(length=20)
	private String fax;

	@Column(length=50)
	private String localidad;

	@Column(nullable=false, length=50)
	private String nombre;

	@Column(name="p_contacto", length=60)
	private String pContacto;

	@Column(length=15)
	private String provincia;

	@Column(length=20)
	private String telefono;

	@Column(length=60)
	private String titulacion;

	@Column(name="tlf_contacto", length=20)
	private String tlfContacto;

	@Column(length=100)
	private String web;

	//bi-directional many-to-one association to Grupo
	@OneToMany(mappedBy="centros")
	private List<Grupo> grupos;

	//bi-directional many-to-one association to Usuario
	@OneToMany(mappedBy="centros")
	private List<Usuario> usuarios;

	public Centro() {
	}

	public int getIdcentro() {
		return this.idcentro;
	}

	public void setIdcentro(int idcentro) {
		this.idcentro = idcentro;
	}

	public String getCod() {
		return this.cod;
	}

	public void setCod(String cod) {
		this.cod = cod;
	}

	public String getCpostal() {
		return this.cpostal;
	}

	public void setCpostal(String cpostal) {
		this.cpostal = cpostal;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getLocalidad() {
		return this.localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPContacto() {
		return this.pContacto;
	}

	public void setPContacto(String pContacto) {
		this.pContacto = pContacto;
	}

	public String getProvincia() {
		return this.provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getTitulacion() {
		return this.titulacion;
	}

	public void setTitulacion(String titulacion) {
		this.titulacion = titulacion;
	}

	public String getTlfContacto() {
		return this.tlfContacto;
	}

	public void setTlfContacto(String tlfContacto) {
		this.tlfContacto = tlfContacto;
	}

	public String getWeb() {
		return this.web;
	}

	public void setWeb(String web) {
		this.web = web;
	}

	public List<Grupo> getGrupos() {
		return this.grupos;
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}

	public Grupo addGrupo(Grupo grupo) {
		getGrupos().add(grupo);
		grupo.setCentros(this);

		return grupo;
	}

	public Grupo removeGrupo(Grupo grupo) {
		getGrupos().remove(grupo);
		grupo.setCentros(null);

		return grupo;
	}

	public List<Usuario> getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario addUsuario(Usuario usuario) {
		getUsuarios().add(usuario);
		usuario.setCentros(this);

		return usuario;
	}

	public Usuario removeUsuario(Usuario usuario) {
		getUsuarios().remove(usuario);
		usuario.setCentros(null);

		return usuario;
	}

}