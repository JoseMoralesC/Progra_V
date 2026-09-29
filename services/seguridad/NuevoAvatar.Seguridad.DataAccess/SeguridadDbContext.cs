using Microsoft.EntityFrameworkCore;
using NuevoAvatar.Seguridad.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NuevoAvatar.Seguridad.DataAccess
{
    public class SeguridadDbContext : DbContext
    {
        public SeguridadDbContext(DbContextOptions<SeguridadDbContext> options)
            : base(options)
        {
        }

        public DbSet<Rol> Roles { get; set; }
        public DbSet<Parametro> Parametros { get; set; }
        public DbSet<Modulo> Modulos { get; set; }
        public DbSet<Usuario> Usuarios { get; set; }
        public DbSet<SesionToken> SesionesToken { get; set; }
        public DbSet<Bitacora> Bitacoras { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Rol>(entity =>
            {
                entity.ToTable("rol", "seguridad");
                entity.HasKey(e => e.IdRol);
                entity.Property(e => e.IdRol).HasColumnName("id_rol");
                entity.Property(e => e.NombreRol).HasColumnName("nombre_rol");
            });

            modelBuilder.Entity<Parametro>(entity =>
            {
                entity.ToTable("parametro", "seguridad");
                entity.HasKey(e => e.IdParametro);
                entity.Property(e => e.IdParametro).HasColumnName("id_parametro");
                entity.Property(e => e.Valor).HasColumnName("valor");
            });

            modelBuilder.Entity<Modulo>(entity =>
            {
                entity.ToTable("modulo", "seguridad");
                entity.HasKey(e => e.IdModulo);
                entity.Property(e => e.IdModulo).HasColumnName("id_modulo");
                entity.Property(e => e.NombreModulo).HasColumnName("nombre_modulo");
            });

            modelBuilder.Entity<Usuario>(entity =>
            {
                entity.ToTable("usuario", "seguridad");
                entity.HasKey(e => e.Email);
                entity.Property(e => e.Email).HasColumnName("email");
                entity.Property(e => e.TipoIdentificacion).HasColumnName("tipo_identificacion");
                entity.Property(e => e.Identificacion).HasColumnName("identificacion");
                entity.Property(e => e.Nombre).HasColumnName("nombre");
                entity.Property(e => e.IdRol).HasColumnName("id_rol");
                entity.Property(e => e.PasswordHash).HasColumnName("password_hash");
            });

            modelBuilder.Entity<SesionToken>(entity =>
            {
                entity.ToTable("refresh_token", "seguridad");
                entity.HasKey(e => e.IdToken);
                entity.Property(e => e.IdToken).HasColumnName("id_token").ValueGeneratedOnAdd();
                entity.Property(e => e.EmailUsuario).HasColumnName("email_usuario");
                entity.Property(e => e.RefreshToken).HasColumnName("refresh_token");
                entity.Property(e => e.FechaCreacion).HasColumnName("fecha_creacion");
                entity.Property(e => e.FechaExpiracion).HasColumnName("fecha_expiracion");
                entity.Property(e => e.Revocado).HasColumnName("revocado");
            });

            modelBuilder.Entity<Bitacora>(entity =>
            {
                entity.ToTable("bitacora", "general");
                entity.HasKey(e => e.IdBitacora);
                entity.Property(e => e.IdBitacora).HasColumnName("id_bitacora").ValueGeneratedOnAdd();
                entity.Property(e => e.Usuario).HasColumnName("usuario");
                entity.Property(e => e.Descripcion).HasColumnName("descripcion");
                entity.Property(e => e.FechaHora).HasColumnName("fecha_hora");
            });
        }
    }
}
