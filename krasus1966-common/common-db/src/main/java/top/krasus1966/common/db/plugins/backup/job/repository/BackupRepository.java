package top.krasus1966.common.db.plugins.backup.job.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import top.krasus1966.common.db.plugins.backup.DatabaseBackupRecord;
import top.krasus1966.common.db.plugins.backup.job.entity.BackupTaskInfo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author krasus1966
 * @date 2024/10/16 11:24
 **/
public class BackupRepository {

    private final JdbcTemplate jdbcTemplate;

    public BackupRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<BackupTaskInfo> findAll() {
        return jdbcTemplate.query("SELECT * FROM sys_database_backup WHERE deleted = 0", new BackupTaskInfoRowMapper());
    }

    public boolean setTaskRunning(String taskId) {

        return jdbcTemplate.update("UPDATE sys_database_backup SET `status` = ? WHERE id = ?",
                BackupTaskInfo.TaskStatus.TASK_RUNNING, taskId) > 0;
    }

    public BackupTaskInfo findById(String id) {
        return jdbcTemplate.queryForObject("SELECT * FROM sys_database_backup WHERE deleted = 0 AND id = ?",
                new BackupTaskInfoRowMapper(), id);
    }

    public boolean updateById(BackupTaskInfo obj) {
        if (obj.getLastSuccessTime() != null) {
            return jdbcTemplate.update("""
                            UPDATE sys_database_backup SET `last_time` = ?,`last_info` = ?,`status` = ? ,`run_count` = ?, `last_duration` = ?,`last_success_time` = ? WHERE id = ?
                            """,
                    obj.getLastTime(), obj.getLastInfo(), obj.getStatus(), obj.getRunCount(), obj.getLastDuration(),
                    obj.getLastSuccessTime(), obj.getId()) > 0;
        }
        return jdbcTemplate.update("""
                         UPDATE sys_database_backup SET `last_time` = ?,`last_info` = ?,`status` = ? ,`run_count` = ?, `last_duration` = ?,`last_success_time` = ? WHERE id = ?
                        """
                , obj.getLastTime(), obj.getLastInfo(), obj.getStatus(), obj.getRunCount(), obj.getLastDuration(),
                obj.getLastSuccessTime(), obj.getId()) > 0;
    }

    public boolean insertRecord(DatabaseBackupRecord record) {
        return jdbcTemplate.update("INSERT sys_database_backup_record (`id`, `crt_time`, `deleted`, `tenant_id`, " +
                        "`db_type`, `db_name`, `file_path`, `err_log`, `success`) VALUES(?,?,?,?,?,?,?,?,?)"
                , record.getId(), record.getCrtTime(), 0, "ttwl", record.getDbType(), record.getDbName(),
                record.getFilePath(),
                record.getErrLog(), record.getSuccess()) > 0;
    }

    private static class BackupTaskInfoRowMapper implements RowMapper<BackupTaskInfo> {
        @Override
        public BackupTaskInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            BackupTaskInfo backupTaskInfo = new BackupTaskInfo();
            backupTaskInfo.setId(rs.getString("id"));
            backupTaskInfo.setTaskName(rs.getString("task_name"));
            backupTaskInfo.setPolicy(rs.getString("policy"));
            backupTaskInfo.setStatus(rs.getInt("status"));
            backupTaskInfo.setLastTime(rs.getDate("last_time"));
            backupTaskInfo.setLastDuration(rs.getLong("last_duration"));
            backupTaskInfo.setLastSuccessTime(rs.getDate("last_success_time"));
            backupTaskInfo.setRunCount(rs.getInt("run_count"));
            backupTaskInfo.setLastInfo(rs.getString("last_info"));
            backupTaskInfo.setDeleted(rs.getInt("deleted"));
            return backupTaskInfo;
        }
    }
}
