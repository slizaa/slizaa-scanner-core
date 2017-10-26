package org.slizaa.addons.mvnresolver.implementation.internal;

import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResult;

// https://github.com/eclipse/aether-demo
public class ResolveArtifact
{

    public static void main( String[] args )
        throws Exception
    {
        System.out.println( "------------------------------------------------------------" );
        System.out.println( ResolveArtifact.class.getSimpleName() );

        RepositorySystem system = Booter.newRepositorySystem();

        RepositorySystemSession session = Booter.newRepositorySystemSession( system );

        Artifact artifact = new DefaultArtifact( "org.eclipse.aether:aether-util:1.0.0.v20140518" );

        ArtifactRequest artifactRequest = new ArtifactRequest();
        artifactRequest.setArtifact( artifact );
        artifactRequest.setRepositories( Booter.newRepositories( system, session ) );

        ArtifactResult artifactResult = system.resolveArtifact( session, artifactRequest );

        artifact = artifactResult.getArtifact();

        System.out.println( artifact + " resolved to  " + artifact.getFile() );
    }

}