# GitHub Issue #5: Implement Cloud Backup & Sync

## Summary
Add comprehensive cloud backup and multi-device synchronization capabilities with support for Google Drive, Dropbox, and OneDrive.

## Description
Enable users to backup their countdown data to the cloud and sync across multiple devices. Includes automatic backups, conflict resolution, and end-to-end encryption options.

## Acceptance Criteria
- [ ] Google Drive integration with OAuth2
- [ ] Automatic scheduled backups
- [ ] Manual backup/restore functionality
- [ ] Multi-device real-time sync
- [ ] Conflict resolution UI
- [ ] Backup encryption with password
- [ ] Offline queue for sync operations
- [ ] Backup history and versioning

## Technical Architecture

### 1. Cloud Provider Abstraction
```kotlin
interface CloudProvider {
    suspend fun authenticate(): AuthResult
    suspend fun upload(data: ByteArray, path: String): UploadResult
    suspend fun download(path: String): ByteArray
    suspend fun list(folder: String): List<CloudFile>
    suspend fun delete(path: String): Boolean
    fun isAuthenticated(): Boolean
}

class GoogleDriveProvider @Inject constructor(
    private val context: Context,
    private val credentialManager: CredentialManager
) : CloudProvider {
    // Implementation using Google Drive API
}

class DropboxProvider @Inject constructor(
    private val context: Context
) : CloudProvider {
    // Implementation using Dropbox SDK
}
```

### 2. Sync Engine
```kotlin
@Singleton
class SyncEngine @Inject constructor(
    private val localDb: CountJoyDatabase,
    private val cloudProvider: CloudProvider,
    private val conflictResolver: ConflictResolver,
    private val encryptionManager: EncryptionManager
) {
    suspend fun performSync(): SyncResult {
        // 1. Get local changes since last sync
        val localChanges = getLocalChangesSinceLastSync()
        
        // 2. Get remote changes
        val remoteChanges = fetchRemoteChanges()
        
        // 3. Detect conflicts
        val conflicts = detectConflicts(localChanges, remoteChanges)
        
        // 4. Resolve conflicts
        val resolutions = if (conflicts.isNotEmpty()) {
            conflictResolver.resolve(conflicts)
        } else emptyList()
        
        // 5. Apply changes
        applyRemoteChanges(remoteChanges)
        pushLocalChanges(localChanges)
        
        return SyncResult(
            synced = localChanges.size + remoteChanges.size,
            conflicts = conflicts.size,
            success = true
        )
    }
}
```

### 3. Backup Manager
```kotlin
class BackupManager @Inject constructor(
    private val database: CountJoyDatabase,
    private val cloudProvider: CloudProvider,
    private val encryptionManager: EncryptionManager
) {
    suspend fun createBackup(
        encrypted: Boolean = false,
        password: String? = null
    ): BackupResult {
        // 1. Export database to JSON
        val backupData = exportToJson()
        
        // 2. Encrypt if requested
        val finalData = if (encrypted && password != null) {
            encryptionManager.encrypt(backupData, password)
        } else backupData
        
        // 3. Upload to cloud
        val filename = generateBackupFilename()
        return cloudProvider.upload(finalData, filename)
    }
    
    suspend fun restoreBackup(
        backupId: String,
        password: String? = null
    ): RestoreResult {
        // 1. Download backup
        val data = cloudProvider.download(backupId)
        
        // 2. Decrypt if needed
        val decrypted = if (isEncrypted(data) && password != null) {
            encryptionManager.decrypt(data, password)
        } else data
        
        // 3. Validate and import
        return importFromJson(decrypted)
    }
}
```

### 4. Conflict Resolution
```kotlin
class ConflictResolver @Inject constructor(
    private val userPreferences: UserPreferences
) {
    suspend fun resolve(conflicts: List<SyncConflict>): List<Resolution> {
        return when (userPreferences.conflictStrategy) {
            Strategy.ASK_USER -> showConflictDialog(conflicts)
            Strategy.PREFER_LOCAL -> conflicts.map { Resolution(it, Choice.LOCAL) }
            Strategy.PREFER_REMOTE -> conflicts.map { Resolution(it, Choice.REMOTE) }
            Strategy.NEWEST_WINS -> conflicts.map { resolveByTimestamp(it) }
            Strategy.MERGE -> conflicts.map { attemptMerge(it) }
        }
    }
}
```

## UI Components

### Cloud Settings Screen
```
Cloud Backup & Sync
├── Cloud Provider
│   ├── Google Drive [Connect/Disconnect]
│   ├── Dropbox [Connect/Disconnect]
│   └── OneDrive [Connect/Disconnect]
├── Automatic Backup
│   ├── Enabled [Toggle]
│   ├── Frequency [Daily/Weekly/Monthly]
│   ├── WiFi Only [Toggle]
│   └── Encrypt Backups [Toggle]
├── Sync Settings
│   ├── Real-time Sync [Toggle]
│   ├── Sync Categories [Select]
│   ├── Conflict Resolution [Strategy]
│   └── Sync Status [Indicator]
├── Backup Management
│   ├── Manual Backup [Button]
│   ├── Restore Backup [Button]
│   └── Backup History [List]
└── Storage
    ├── Used Space [Progress]
    └── Clear Cache [Button]
```

### Conflict Resolution Dialog
```kotlin
@Composable
fun ConflictResolutionDialog(
    conflicts: List<SyncConflict>,
    onResolve: (List<Resolution>) -> Unit
) {
    Dialog {
        Column {
            Text("Sync Conflicts Detected")
            
            LazyColumn {
                items(conflicts) { conflict ->
                    ConflictCard(
                        conflict = conflict,
                        onChoice = { choice ->
                            // Handle individual choice
                        }
                    )
                }
            }
            
            Row {
                Button("Use Local for All")
                Button("Use Remote for All")
                Button("Resolve Individually")
            }
        }
    }
}
```

## Implementation Steps

1. **Phase 1: Cloud Provider Setup (2 days)**
   - OAuth2 implementation
   - Google Drive API integration
   - Provider abstraction layer

2. **Phase 2: Backup System (2 days)**
   - Export/Import functionality
   - Encryption implementation
   - Backup scheduling with WorkManager

3. **Phase 3: Sync Engine (3 days)**
   - Change tracking system
   - Sync algorithm implementation
   - Conflict detection logic

4. **Phase 4: UI Implementation (2 days)**
   - Settings screens
   - Conflict resolution UI
   - Sync status indicators

5. **Phase 5: Testing & Optimization (2 days)**
   - Multi-device testing
   - Performance optimization
   - Error handling

## Database Schema
```sql
CREATE TABLE sync_metadata (
    id INTEGER PRIMARY KEY,
    entity_type TEXT NOT NULL,
    entity_id INTEGER NOT NULL,
    last_modified TIMESTAMP NOT NULL,
    sync_version INTEGER NOT NULL,
    device_id TEXT NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    UNIQUE(entity_type, entity_id)
);

CREATE TABLE backup_history (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    backup_id TEXT UNIQUE NOT NULL,
    provider TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    size_bytes INTEGER,
    is_encrypted BOOLEAN DEFAULT FALSE,
    event_count INTEGER,
    status TEXT NOT NULL
);

CREATE TABLE sync_queue (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    operation TEXT NOT NULL,
    entity_type TEXT NOT NULL,
    entity_id INTEGER NOT NULL,
    data TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    retry_count INTEGER DEFAULT 0
);
```

## Security Considerations
- OAuth2 tokens stored in Android Keystore
- Optional end-to-end encryption with AES-256
- Password-based key derivation (PBKDF2)
- Secure deletion of old backups
- Certificate pinning for API calls

## Testing Checklist
- [ ] OAuth flow completes successfully
- [ ] Backups upload and download correctly
- [ ] Encryption/decryption works properly
- [ ] Sync handles offline scenarios
- [ ] Conflicts are detected accurately
- [ ] Multi-device sync maintains consistency
- [ ] Performance meets requirements
- [ ] Security measures are effective

## Performance Requirements
- Backup time: < 30s for 1000 events
- Sync latency: < 5s for changes
- Bandwidth usage: < 5MB per sync
- Battery impact: < 2% daily

## Dependencies
- Google Play Services (for Drive)
- Dropbox SDK
- Microsoft Graph SDK (OneDrive)
- WorkManager for scheduling
- SQLCipher for encryption

## Priority
🔴 **HIGH** - Critical for data safety

## Labels
`enhancement`, `cloud`, `sync`, `backup`, `high-priority`

## Milestone
v2.3.0 - Cloud Integration

## Estimated Time
11 days

## Related PRDs
- [Data Management & Sync PRD](../PRDs/05-data-management-prd.md)

## Notes
- Consider adding iCloud support for iOS version
- Plan for bandwidth optimization
- Add telemetry for sync success rates