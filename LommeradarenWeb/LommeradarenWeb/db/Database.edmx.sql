
-- --------------------------------------------------
-- Entity Designer DDL Script for SQL Server 2005, 2008, 2012 and Azure
-- --------------------------------------------------
-- Date Created: 03/20/2014 11:03:54
-- Generated from EDMX file: C:\Users\Pers\workspace\Lommeradaren\LommeradarenWeb\LommeradarenWeb\db\Database.edmx
-- --------------------------------------------------

SET QUOTED_IDENTIFIER OFF;
GO
USE [LommeradarDB];
GO
IF SCHEMA_ID(N'dbo') IS NULL EXECUTE(N'CREATE SCHEMA [dbo]');
GO

-- --------------------------------------------------
-- Dropping existing FOREIGN KEY constraints
-- --------------------------------------------------

IF OBJECT_ID(N'[dbo].[FK_PointOfInterestPicture]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[Pictures] DROP CONSTRAINT [FK_PointOfInterestPicture];
GO
IF OBJECT_ID(N'[dbo].[FK_UserPicture]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[Pictures] DROP CONSTRAINT [FK_UserPicture];
GO

-- --------------------------------------------------
-- Dropping existing tables
-- --------------------------------------------------

IF OBJECT_ID(N'[dbo].[Users]', 'U') IS NOT NULL
    DROP TABLE [dbo].[Users];
GO
IF OBJECT_ID(N'[dbo].[Pictures]', 'U') IS NOT NULL
    DROP TABLE [dbo].[Pictures];
GO
IF OBJECT_ID(N'[dbo].[PointOfInterest]', 'U') IS NOT NULL
    DROP TABLE [dbo].[PointOfInterest];
GO

-- --------------------------------------------------
-- Creating all tables
-- --------------------------------------------------

-- Creating table 'Users'
CREATE TABLE [dbo].[Users] (
    [UserID] int IDENTITY(1,1) NOT NULL,
    [UserEmail] nvarchar(max)  NULL,
    [UserPassword] nvarchar(max)  NULL,
    [UserName] nvarchar(max)  NULL
);
GO

-- Creating table 'Pictures'
CREATE TABLE [dbo].[Pictures] (
    [PictureID] int IDENTITY(1,1) NOT NULL,
    [UserUserID] int  NOT NULL,
    [PointOfInterestPOI_ID] int  NOT NULL,
    [Picutre] nvarchar(max)  NULL,
    [Latitude] float  NULL,
    [Longitude] float  NULL,
    [Timestamp] datetime  NULL
);
GO

-- Creating table 'PointOfInterest'
CREATE TABLE [dbo].[PointOfInterest] (
    [POI_ID] int IDENTITY(1,1) NOT NULL,
    [name] nvarchar(max)  NULL,
    [imo] nvarchar(max)  NULL,
    [mmsi] nvarchar(max)  NULL,
    [webpage] nvarchar(max)  NULL
);
GO

-- --------------------------------------------------
-- Creating all PRIMARY KEY constraints
-- --------------------------------------------------

-- Creating primary key on [UserID] in table 'Users'
ALTER TABLE [dbo].[Users]
ADD CONSTRAINT [PK_Users]
    PRIMARY KEY CLUSTERED ([UserID] ASC);
GO

-- Creating primary key on [PictureID] in table 'Pictures'
ALTER TABLE [dbo].[Pictures]
ADD CONSTRAINT [PK_Pictures]
    PRIMARY KEY CLUSTERED ([PictureID] ASC);
GO

-- Creating primary key on [POI_ID] in table 'PointOfInterest'
ALTER TABLE [dbo].[PointOfInterest]
ADD CONSTRAINT [PK_PointOfInterest]
    PRIMARY KEY CLUSTERED ([POI_ID] ASC);
GO

-- --------------------------------------------------
-- Creating all FOREIGN KEY constraints
-- --------------------------------------------------

-- Creating foreign key on [PointOfInterestPOI_ID] in table 'Pictures'
ALTER TABLE [dbo].[Pictures]
ADD CONSTRAINT [FK_PointOfInterestPicture]
    FOREIGN KEY ([PointOfInterestPOI_ID])
    REFERENCES [dbo].[PointOfInterest]
        ([POI_ID])
    ON DELETE NO ACTION ON UPDATE NO ACTION;

-- Creating non-clustered index for FOREIGN KEY 'FK_PointOfInterestPicture'
CREATE INDEX [IX_FK_PointOfInterestPicture]
ON [dbo].[Pictures]
    ([PointOfInterestPOI_ID]);
GO

-- Creating foreign key on [UserUserID] in table 'Pictures'
ALTER TABLE [dbo].[Pictures]
ADD CONSTRAINT [FK_UserPicture]
    FOREIGN KEY ([UserUserID])
    REFERENCES [dbo].[Users]
        ([UserID])
    ON DELETE NO ACTION ON UPDATE NO ACTION;

-- Creating non-clustered index for FOREIGN KEY 'FK_UserPicture'
CREATE INDEX [IX_FK_UserPicture]
ON [dbo].[Pictures]
    ([UserUserID]);
GO

-- --------------------------------------------------
-- Script has ended
-- --------------------------------------------------